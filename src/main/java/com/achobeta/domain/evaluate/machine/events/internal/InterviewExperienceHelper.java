package com.achobeta.domain.evaluate.machine.events.internal;

import com.achobeta.common.enums.EmailTemplateEnum;
import com.achobeta.common.enums.InterviewEvent;
import com.achobeta.common.enums.InterviewStatus;
import com.achobeta.domain.html.model.po.HtmlResource;
import com.achobeta.domain.email.model.po.EmailMessage;
import com.achobeta.domain.html.model.po.MarkdownReplaceResource;
import com.achobeta.domain.html.service.HtmlEngine;
import com.achobeta.domain.email.service.EmailSender;
import com.achobeta.domain.evaluate.model.vo.InterviewExperienceTemplateClose;
import com.achobeta.domain.evaluate.model.vo.InterviewExperienceTemplateInner;
import com.achobeta.domain.evaluate.model.vo.InterviewExperienceTemplateOpen;
import com.achobeta.domain.evaluate.service.InterviewQuestionScoreService;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.achobeta.domain.interview.machine.events.internal.InterviewStateInternalTransitionHelper;
import com.achobeta.domain.interview.model.vo.InterviewDetailVO;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.schedule.model.vo.ScheduleVO;
import com.achobeta.domain.schedule.service.InterviewScheduleService;
import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-29
 * Time: 2:05
 */
@Component
@RequiredArgsConstructor
public class InterviewExperienceHelper implements InterviewStateInternalTransitionHelper {

    @Value("${spring.mail.username}")
    private String achobetaEmail;

    private final EmailSender emailSender;

    private final HtmlEngine htmlEngine;

    private final Condition<InterviewContext> defaultInterviewCondition;

    private final Action<InterviewStatus, InterviewEvent, InterviewContext> defaultInterviewAction;

    private final InterviewQuestionScoreService interviewQuestionScoreService;

    private final InterviewService interviewService;

    private final InterviewScheduleService interviewScheduleService;

    @Override
    public List<InterviewStatus> getWithin() {
        return List.of(
                InterviewStatus.ENDED
        );
    }

    @Override
    public InterviewEvent getOnEvent() {
        return InterviewEvent.INTERVIEW_EXPERIENCE;
    }

    @Override
    public Condition<InterviewContext> getWhenCondition() {
        return defaultInterviewCondition;
    }

    @Override
    public Action<InterviewStatus, InterviewEvent, InterviewContext> getPerformAction() {
        return (from, to, event, context) -> {
            defaultInterviewAction.execute(from, to, event, context);

            // 准备数据
            Long interviewId = context.getInterview().getId();
            InterviewDetailVO interviewDetail = interviewService.getInterviewDetail(interviewId);
            ScheduleVO interviewDetailScheduleVO = interviewDetail.getScheduleVO();
            SimpleStudentVO simpleStudentVO = interviewScheduleService
                    .getDetailActivityParticipation(interviewDetailScheduleVO.getParticipationId())
                    .getSimpleStudentVO();

            // 构造邮件消息
            EmailMessage emailMessage = new EmailMessage();
            emailMessage.setSender(achobetaEmail);
            emailMessage.setCarbonCopy();
            emailMessage.setCreateTime(new Date());
            emailMessage.setTitle(EmailTemplateEnum.INTERVIEW_EXPERIENCE_OPEN.getTitle());
            emailMessage.setRecipient(simpleStudentVO.getEmail());

            String openTemplate = EmailTemplateEnum.INTERVIEW_EXPERIENCE_OPEN.getTemplate();
            InterviewExperienceTemplateOpen templateOpen = InterviewExperienceTemplateOpen.builder()
                    .studentId(simpleStudentVO.getStudentId())
                    .title(interviewDetail.getTitle())
                    .build();

            String innerTemplate = EmailTemplateEnum.INTERVIEW_EXPERIENCE_INNER.getTemplate();
            List<MarkdownReplaceResource> replaceResourceList = new ArrayList<>();
            List<HtmlResource> htmlResourceList =  interviewQuestionScoreService.getInterviewPaperDetail(interviewId)
                    .getQuestions()
                    .stream()
                    .map(question -> {
                        String target = htmlEngine.getUniqueSymbol();
                        String standard = question.getStandard();
                        replaceResourceList.add(new MarkdownReplaceResource(target, standard));
                        return InterviewExperienceTemplateInner.builder()
                                .title(question.getTitle())
                                .score(question.getScore())
                                .average(question.getAverage())
                                .standard(target)
                                .build();
                    }).map(inner -> {
                        return new HtmlResource(innerTemplate, inner);
                    }).toList();

            String closeTemplate = EmailTemplateEnum.INTERVIEW_EXPERIENCE_CLOSE.getTemplate();
            InterviewExperienceTemplateClose templateClose = InterviewExperienceTemplateClose.builder()
                    .startTime(interviewDetailScheduleVO.getStartTime())
                    .endTime(interviewDetailScheduleVO.getEndTime())
                    .build();

            // 构造 html
            String html = htmlEngine.builder()
                    .append(openTemplate, templateOpen)
                    .append(htmlResourceList)
                    .append(closeTemplate, templateClose)
                    .replaceMarkdown(replaceResourceList)
                    .build();

            // 发送邮件
            emailSender.sendModelMail(emailMessage, html);
        };
    }
}
