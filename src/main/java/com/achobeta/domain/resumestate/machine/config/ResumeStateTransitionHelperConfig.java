package com.achobeta.domain.resumestate.machine.config;

import com.achobeta.common.enums.EmailTemplateEnum;
import com.achobeta.common.enums.ResumeEvent;
import com.achobeta.common.enums.ResumeStatus;
import com.achobeta.domain.resumestate.machine.context.ResumeContext;
import com.achobeta.domain.resumestate.model.dto.ResumeExecuteDTO;
import com.achobeta.domain.resumestate.model.vo.ResumeNoticeTemplate;
import com.achobeta.domain.student.model.entity.StuResume;
import com.achobeta.email.EmailSender;
import com.achobeta.email.model.po.EmailMessage;
import com.achobeta.template.engine.HtmlEngine;
import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 1:55
 */
@Configuration
@RequiredArgsConstructor
public class ResumeStateTransitionHelperConfig {

    @Value("${spring.mail.username}")
    private String achobetaEmail;

    private final HtmlEngine htmlEngine;

    private final EmailSender emailSender;

    @Bean
    public Condition<ResumeContext> defaultResumeCondition() {
        return resumeContext -> Boolean.TRUE;
    }

    @Bean
    public Action<ResumeStatus, ResumeEvent, ResumeContext> defaultResumeAction() {
        return (from, to, event, context) -> {
            context.log(from, to, event);
            context.getResume().setStatus(to);
            context.setHit(Boolean.TRUE);
        };
    }

    /**
     * 建议在简历状态流转 action 的最后一步执行这个 resumeNotice 的 action，这样就代表轮转成功才发通知，否则会骚扰用户
     */
    @Bean
    public Action<ResumeStatus, ResumeEvent, ResumeContext> resumeNotice() {
        return (from, to, event, context) -> {
            Optional.ofNullable(context.getExecuteDTO()).map(ResumeExecuteDTO::getIsNotified).filter(Boolean.TRUE::equals).ifPresent(isNotified -> {
                StuResume currentResume = context.getResume();
                // 封装 email
                EmailTemplateEnum emailTemplate = EmailTemplateEnum.RESUME_NOTICE;
                EmailMessage emailMessage = new EmailMessage();
                emailMessage.setSender(achobetaEmail);
                emailMessage.setCarbonCopy();
                emailMessage.setCreateTime(new Date());
                emailMessage.setTitle(emailTemplate.getTitle());
                emailMessage.setRecipient(currentResume.getEmail());
                // 构造模板消息
                ResumeNoticeTemplate resumeNoticeTemplate = ResumeNoticeTemplate.builder()
                        .studentId(currentResume.getStudentId())
                        .status(to.getMessage())
                        .event(event.getRemark())
                        .build();
                String html = htmlEngine.builder()
                        .append(emailTemplate.getTemplate(), resumeNoticeTemplate)
                        .build();
                emailMessage.setContent(html);
                // 发送模板消息
                emailSender.send(emailMessage);
            });
        };
    }
}
