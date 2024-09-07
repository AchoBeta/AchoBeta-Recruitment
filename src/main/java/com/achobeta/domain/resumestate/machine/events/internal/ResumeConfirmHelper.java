package com.achobeta.domain.resumestate.machine.events.internal;

import com.achobeta.common.enums.EmailTemplateEnum;
import com.achobeta.common.enums.ResumeEvent;
import com.achobeta.common.enums.ResumeStatus;
import com.achobeta.domain.email.model.po.EmailMessage;
import com.achobeta.domain.email.service.EmailSender;
import com.achobeta.domain.templateengine.service.HtmlEngine;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;
import com.achobeta.domain.resumestate.model.dto.MemberDTO;
import com.achobeta.domain.resumestate.model.dto.ResumeExecuteDTO;
import com.achobeta.domain.resumestate.model.entity.Member;
import com.achobeta.domain.resumestate.model.vo.ConfirmationNoticeTemplate;
import com.achobeta.domain.resumestate.service.MemberService;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.util.ValidatorUtils;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 14:12
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class
ResumeConfirmHelper implements ResumeStateInternalTransitionHelper{

    @Value("${spring.mail.username}")
    private String achobetaEmail;

    private final EmailSender emailSender;

    private final HtmlEngine htmlEngine;

    private final MemberService memberService;

    private final Action<ResumeStatus, ResumeEvent, ResumeContext> defaultResumeAction;

    @Override
    public List<ResumeStatus> getWithin() {
        return List.of(
                ResumeStatus.INITIAL_INTERVIEW_PASSED,
                ResumeStatus.SECOND_INTERVIEW_PASSED,
                ResumeStatus.FINAL_INTERVIEW_PASSED
        );
    }

    @Override
    public ResumeEvent getOnEvent() {
        return ResumeEvent.CONFIRM;
    }

    @Override
    public Condition<ResumeContext> getWhenCondition() {
        return resumeContext -> {
            return Optional.ofNullable(resumeContext)
                    .map(ResumeContext::getExecuteDTO)
                    .map(ResumeExecuteDTO::getMemberDTO)
                    .map(memberDTO -> {
                        ValidatorUtils.validate(memberDTO);
                        return Boolean.TRUE;
                    })
                    .orElse(Boolean.FALSE);
        };
    }

    @Override
    public Action<ResumeStatus, ResumeEvent, ResumeContext> getPerformAction() {
        return (from, to, event, context) -> {
            defaultResumeAction.execute(from, to, event, context);
            // 创建新成员
            StuResume currentResume = context.getResume();
            MemberDTO memberDTO = context.getExecuteDTO().getMemberDTO();
            Member member = memberService.createMember(
                    currentResume.getId(),
                    context.getManagerId(),
                    memberDTO
            );
            log.info("create a new member success: {}", member);
            // 封装 email
            EmailTemplateEnum emailTemplate = EmailTemplateEnum.MEMBER_NOTICE;
            EmailMessage emailMessage = new EmailMessage();
            emailMessage.setSender(achobetaEmail);
            emailMessage.setCarbonCopy();
            emailMessage.setCreateTime(new Date());
            emailMessage.setTitle(emailTemplate.getTitle());
            emailMessage.setRecipient(currentResume.getEmail());
            // 构造模板消息
            ConfirmationNoticeTemplate confirmationNoticeTemplate = ConfirmationNoticeTemplate.builder()
                    .studentId(currentResume.getStudentId())
                    .username(memberDTO.getUsername())
                    .password(memberDTO.getPassword())
                    .build();
            String html = htmlEngine.builder()
                    .append(emailTemplate.getTemplate(), confirmationNoticeTemplate)
                    .build();
            // 发送
            emailSender.sendModelMail(emailMessage, html);
        };
    }
}
