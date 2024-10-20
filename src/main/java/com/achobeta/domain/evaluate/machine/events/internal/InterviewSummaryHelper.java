package com.achobeta.domain.evaluate.machine.events.internal;

import com.achobeta.domain.evaluate.model.vo.InterviewSummaryTemplate;
import com.achobeta.domain.evaluate.model.vo.InterviewSummaryVO;
import com.achobeta.domain.evaluate.service.InterviewSummaryService;
import com.achobeta.domain.interview.enums.InterviewEvent;
import com.achobeta.domain.interview.enums.InterviewStatus;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.achobeta.domain.interview.machine.events.internal.InterviewStateInternalTransitionHelper;
import com.achobeta.domain.interview.model.vo.InterviewDetailVO;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import com.achobeta.email.enums.EmailTemplateEnum;
import com.achobeta.email.model.po.EmailMessage;
import com.achobeta.email.sender.EmailSender;
import com.achobeta.template.engine.HtmlEngine;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 19:44
 */
@Component
@RequiredArgsConstructor
public class InterviewSummaryHelper implements InterviewStateInternalTransitionHelper {

    private final EmailSender emailSender;

    private final HtmlEngine htmlEngine;

    private final Condition<InterviewContext> defaultInterviewCondition;

    private final Action<InterviewStatus, InterviewEvent, InterviewContext> defaultInterviewAction;

    private final InterviewSummaryService interviewSummaryService;

    private final InterviewService interviewService;

    @Override
    public List<InterviewStatus> getWithin() {
        return List.of(
                InterviewStatus.ENDED
        );
    }

    @Override
    public InterviewEvent getOnEvent() {
        return InterviewEvent.INTERVIEW_SUMMARY;
    }

    @Override
    public Condition<InterviewContext> getWhenCondition() {
        return defaultInterviewCondition;
    }

    @Override
    public Action<InterviewStatus, InterviewEvent, InterviewContext> getPerformAction() {
        return (from, to, event, context) -> {
            defaultInterviewAction.execute(from, to, event, context);
            // 获得数据
            Long interviewId = context.getInterview().getId();
            InterviewSummaryVO interviewSummaryVO = interviewSummaryService.queryInterviewSummaryByInterviewId(interviewId);
            InterviewDetailVO interviewDetail = interviewService.getInterviewDetail(interviewId);
            SimpleStudentVO simpleStudentVO = interviewDetail.getSimpleStudentVO();
            // 封装 email
            EmailTemplateEnum emailTemplate = EmailTemplateEnum.INTERVIEW_SUMMARY;
            EmailMessage emailMessage = new EmailMessage();
            emailMessage.setTitle(emailTemplate.getTitle());
            emailMessage.setRecipient(simpleStudentVO.getEmail());
            // 构造模板消息
            InterviewSummaryTemplate interviewSummaryTemplate = InterviewSummaryTemplate.builder()
                    .studentId(simpleStudentVO.getStudentId())
                    .title(interviewDetail.getTitle())
                    .basis(interviewSummaryVO.getBasis())
                    .coding(interviewSummaryVO.getCoding())
                    .thinking(interviewSummaryVO.getThinking())
                    .express(interviewSummaryVO.getExpress())
                    .evaluate(interviewSummaryVO.getEvaluate())
                    .suggest(interviewSummaryVO.getSuggest())
                    .playback(interviewSummaryVO.getPlayback())
                    .build();
            String html = htmlEngine.builder()
                    .append(emailTemplate.getTemplate(), interviewSummaryTemplate)
                    .build();
            emailMessage.setContent(html);
            // 发送模板消息
            emailSender.send(emailMessage);
        };
    }
}
