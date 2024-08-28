package com.achobeta.domain.email.service;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.email.model.po.EmailMessage;
import com.achobeta.domain.email.model.vo.EmailCenterTemplate;
import com.achobeta.domain.email.util.EmailTemplateUtil;
import com.achobeta.exception.GlobalServiceException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    public SimpleMailMessage emailToSimpleMailMessage(@NonNull EmailMessage emailMessage) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailMessage.getSender());
        simpleMailMessage.setTo(emailMessage.getRecipient());
        simpleMailMessage.setCc(emailMessage.getCarbonCopy());
        simpleMailMessage.setSentDate(emailMessage.getCreateTime());
        simpleMailMessage.setSubject(emailMessage.getTitle());
        simpleMailMessage.setText(emailMessage.getContent());
        return simpleMailMessage;
    }

    public MimeMessageHelper emailIntoMimeMessageByHelper(MimeMessage mimeMessage, @NonNull EmailMessage emailMessage) {
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(emailMessage.getSender());
            mimeMessageHelper.setCc(emailMessage.getCarbonCopy());
            mimeMessageHelper.setSubject(emailMessage.getTitle());
            mimeMessageHelper.setSentDate(emailMessage.getCreateTime());
            mimeMessageHelper.setTo(emailMessage.getRecipient());
            return mimeMessageHelper;
        } catch (MessagingException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.EMAIL_SEND_FAIL);
        }
    }

    public void sendSimpleMailMessage(@NonNull EmailMessage emailMessage) {
        // 封装simpleMailMessage对象
        SimpleMailMessage simpleMailMessage = emailToSimpleMailMessage(emailMessage);
        // 发送
        javaMailSender.send(simpleMailMessage);
    }

    public void sendMailWithFile(@NonNull EmailMessage emailMessage, File... files) {
        // 封装对象
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = emailIntoMimeMessageByHelper(mimeMessage, emailMessage);
            // 添加附件
            for (File file : files) {
                if (Objects.nonNull(file)) {
                    mimeMessageHelper.addAttachment(file.getName(), file);
                }
            }
            mimeMessageHelper.setText(emailMessage.getContent(), false);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.EMAIL_SEND_FAIL);
        }
    }

    public void sendModelMail(@NonNull EmailMessage emailMessage, String template, Object modelMessage) {
        // 封装对象
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = emailIntoMimeMessageByHelper(mimeMessage, emailMessage);
            // 构造模板消息
            Context context = new Context();
            context.setVariables(BeanUtil.beanToMap(modelMessage));
            //合并模板与数据
            String content = templateEngine.process(template, context);
            mimeMessageHelper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.EMAIL_SEND_FAIL);
        }
    }

    public void sendModelMail(@NonNull EmailMessage emailMessage,
                              String templateOpen, Object modelMessageOpen,
                              List<EmailCenterTemplate> emailCenterTemplateList,
                              String templateClose, Object modelMessageClose) {
        // 封装对象
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = emailIntoMimeMessageByHelper(mimeMessage, emailMessage);
            // 构造模板消息
            Context contextOpen = new Context();
            contextOpen.setVariables(BeanUtil.beanToMap(modelMessageOpen));
            Context contextClose = new Context();
            contextOpen.setVariables(BeanUtil.beanToMap(modelMessageClose));
            //合并模板与数据
            String content = EmailTemplateUtil.getHtmlBuilder()
                    .append(templateEngine.process(templateOpen, contextOpen))
                    .group(emailCenterTemplateList)
                    .append(templateEngine.process(templateClose, contextClose))
                    .builder();
            mimeMessageHelper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.EMAIL_SEND_FAIL);
        }
    }

    public void sendModelMailWithFile(@NonNull EmailMessage emailMessage, String template, Object modelMessage, File... files) {
        // 封装对象
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = emailIntoMimeMessageByHelper(mimeMessage, emailMessage);
            // 构造模板消息
            Context context = new Context();
            context.setVariables(BeanUtil.beanToMap(modelMessage));
            //合并模板与数据
            String content = templateEngine.process(template, context);
            mimeMessageHelper.setText(content, true);
            // 添加附件
            for (File file : files) {
                if (Objects.nonNull(file)) {
                    mimeMessageHelper.addAttachment(file.getName(), file);
                }
            }
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.EMAIL_SEND_FAIL);
        }
    }

    public <T, R> void customizedSendEmail(@NonNull EmailMessage emailMessage, String template, Function<String, R> function, File... files) {
        String sender = emailMessage.getSender();
        String[] carbonCopy = emailMessage.getCarbonCopy();
        String title = emailMessage.getTitle();
        Arrays.stream(emailMessage.getRecipient())
                .parallel()
                .distinct()
                .forEach(s -> {
                    try {
                        // 封装对象
                        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                        mimeMessageHelper.setTo(s);
                        mimeMessageHelper.setFrom(sender);
                        mimeMessageHelper.setCc(carbonCopy);
                        mimeMessageHelper.setSubject(title);
                        // 添加附件
                        for (File file : files) {
                            if (Objects.nonNull(file)) {
                                mimeMessageHelper.addAttachment(file.getName(), file);
                            }
                        }
                        // 构造模板消息
                        Context context = new Context();
                        Object modelMessage = function.apply(s);
                        context.setVariables(BeanUtil.beanToMap(modelMessage));
                        //合并模板与数据
                        String content = templateEngine.process(template, context);
                        // 通过mimeMessageHelper设置到mimeMessage里
                        mimeMessageHelper.setText(content, true);
                        //发送
                        javaMailSender.send(mimeMessage);
                    } catch (MessagingException e) {
                        throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.EMAIL_SEND_FAIL);
                    }
                });
    }

}
