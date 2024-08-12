package com.achobeta.domain.interview.machine.events.internal;

import com.achobeta.common.enums.EmailTemplateEnum;
import com.achobeta.common.enums.InterviewStateEvent;
import com.achobeta.common.enums.InterviewStatusEnum;
import com.achobeta.domain.email.model.po.EmailMessage;
import com.achobeta.domain.email.service.EmailSender;
import com.achobeta.domain.interview.machine.context.InterviewContext;
import com.achobeta.domain.interview.machine.constants.InterviewStateMachineConstants;
import com.achobeta.domain.interview.model.entity.Interview;
import com.achobeta.domain.interview.model.vo.InterviewDetailVO;
import com.achobeta.domain.interview.model.vo.InterviewNoticeTemplate;
import com.achobeta.domain.interview.service.InterviewService;
import com.achobeta.domain.schedule.model.vo.ParticipationDetailVO;
import com.achobeta.domain.schedule.model.vo.ScheduleVO;
import com.achobeta.domain.schedule.service.InterviewScheduleService;
import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 13:41
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InterviewStateNoticeHelper implements InterviewStateInternalTransitionHelper{

    @Value("${spring.mail.username}")
    private String achobetaEmail;

    private final EmailSender emailSender;

    private final InterviewService interviewService;

    private final InterviewScheduleService interviewScheduleService;

    @Override
    public List<InterviewStatusEnum> getWithinList() {
        return List.of(InterviewStatusEnum.values());
    }

    @Override
    public InterviewStateEvent getOnEvent() {
        return InterviewStateEvent.INTERVIEW_STARTING_NOTICE;
    }

    @Override
    public Condition<InterviewContext> getWhenCondition() {
        return InterviewStateMachineConstants.DEFAULT_CONDITION();
    }

    @Override
    public Action<InterviewStatusEnum, InterviewStateEvent, InterviewContext> getPerformAction() {
        return (from, to, event, context) -> {
            Long managerId = context.getManagerId();
            Interview currentInterview = context.getInterview();
            log.info("state from {} to {} run {} currentInterview {} managerId {}",
                    from, to, event, currentInterview.getId(), managerId);
            // 获得数据
            InterviewDetailVO interviewDetail = interviewService.getInterviewDetail(currentInterview.getId());
            ScheduleVO scheduleVO = interviewDetail.getScheduleVO();
            ParticipationDetailVO detailActivityParticipation = interviewScheduleService.getDetailActivityParticipation(scheduleVO.getParticipationId());
            SimpleStudentVO simpleStudentVO = detailActivityParticipation.getSimpleStudentVO();
            // 封装 email
            EmailMessage emailMessage = new EmailMessage();
            emailMessage.setSender(achobetaEmail);
            emailMessage.setCarbonCopy();
            emailMessage.setCreateTime(new Date());
            emailMessage.setTitle(EmailTemplateEnum.INTERVIEW_NOTICE.getTitle());
            emailMessage.setRecipient(simpleStudentVO.getEmail());
            // 构造模板消息
            InterviewNoticeTemplate interviewNoticeTemplate = InterviewNoticeTemplate.builder()
                    .studentId(simpleStudentVO.getStudentId())
                    .title(interviewDetail.getTitle())
                    .description(interviewDetail.getDescription())
                    .address(interviewDetail.getAddress())
                    .startTime(scheduleVO.getStartTime())
                    .endTime(scheduleVO.getEndTime())
                    .status(interviewDetail.getStatus())
                    .build();
            // 发送
            emailSender.sendModelMail(emailMessage, EmailTemplateEnum.INTERVIEW_NOTICE.getTemplate(), interviewNoticeTemplate);
        };
    }
}
