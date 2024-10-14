package com.achobeta.domain.evaluate.machine.events.internal;

import com.achobeta.domain.evaluate.model.vo.InterviewExperienceTemplate;
import com.achobeta.domain.evaluate.model.vo.InterviewExperienceTemplateInner;
import com.achobeta.domain.evaluate.service.InterviewQuestionScoreService;
import com.achobeta.domain.interview.enums.InterviewEvent;
import com.achobeta.domain.interview.enums.InterviewStatus;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.achobeta.domain.interview.machine.events.internal.InterviewStateInternalTransitionHelper;
import com.achobeta.domain.interview.model.vo.InterviewDetailVO;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.schedule.model.vo.ScheduleVO;
import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import com.achobeta.email.enums.EmailTemplateEnum;
import com.achobeta.email.model.po.EmailMessage;
import com.achobeta.email.sender.EmailSender;
import com.achobeta.template.engine.HtmlEngine;
import com.achobeta.template.model.po.ReplaceResource;
import com.achobeta.template.util.TemplateUtil;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
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

    private final EmailSender emailSender;

    private final HtmlEngine htmlEngine;

    private final Condition<InterviewContext> defaultInterviewCondition;

    private final Action<InterviewStatus, InterviewEvent, InterviewContext> defaultInterviewAction;

    private final InterviewQuestionScoreService interviewQuestionScoreService;

    private final InterviewService interviewService;

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
            SimpleStudentVO simpleStudentVO = interviewDetail.getSimpleStudentVO();

            // 构造邮件消息
            EmailTemplateEnum emailTemplate = EmailTemplateEnum.INTERVIEW_EXPERIENCE;
            EmailMessage emailMessage = new EmailMessage();
            emailMessage.setTitle(emailTemplate.getTitle());
            emailMessage.setRecipient(simpleStudentVO.getEmail());

            List<ReplaceResource> replaceResourceList = new LinkedList<>();
            List<InterviewExperienceTemplateInner> inners = interviewQuestionScoreService.getInterviewPaperDetail(interviewId)
                    .getQuestions()
                    .stream()
                    .map(question -> {
                        String target = TemplateUtil.getUniqueSymbol();
                        InterviewExperienceTemplateInner inner = InterviewExperienceTemplateInner.builder()
                                .title(question.getTitle())
                                .score(question.getScore())
                                .average(question.getAverage())
                                .standard(target)
                                .build();
                        replaceResourceList.add(new ReplaceResource(target, question.getStandard()));
                        return inner;
                    }).toList();
            InterviewExperienceTemplate interviewExperience = InterviewExperienceTemplate.builder()
                    .studentId(simpleStudentVO.getStudentId())
                    .title(interviewDetail.getTitle())
                    .inners(inners)
                    .startTime(interviewDetailScheduleVO.getStartTime())
                    .endTime(interviewDetailScheduleVO.getEndTime())
                    .build();

            // 构造 html
            String html = htmlEngine.builder()
                    .append(emailTemplate.getTemplate(), interviewExperience)
                    .replaceMarkdown(replaceResourceList)
                    .build();
            emailMessage.setContent(html);
            // 发送模板消息
            emailSender.send(emailMessage);
        };
    }
}
