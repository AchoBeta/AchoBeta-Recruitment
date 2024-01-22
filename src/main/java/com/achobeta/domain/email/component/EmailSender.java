package com.achobeta.domain.email.component;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.email.component.po.EmailMessage;
import com.achobeta.exception.GlobalServiceException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
import java.util.Objects;
import java.util.function.Function;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    public SimpleMailMessage emailToSimpleMailMessage(EmailMessage emailMessage) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailMessage.getSender());
        simpleMailMessage.setTo(emailMessage.getRecipient());
        simpleMailMessage.setCc(emailMessage.getCarbonCopy());
        simpleMailMessage.setSubject(emailMessage.getTitle());
        simpleMailMessage.setText(emailMessage.getContent());
        return simpleMailMessage;
    }

    public MimeMessageHelper emailIntoMimeMessageByHelper(MimeMessage mimeMessage, EmailMessage emailMessage) {
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(emailMessage.getSender());
            mimeMessageHelper.setCc(emailMessage.getCarbonCopy());
            mimeMessageHelper.setSubject(emailMessage.getTitle());
            mimeMessageHelper.setTo(emailMessage.getRecipient());
            return mimeMessageHelper;
        } catch (MessagingException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.EMAIL_SEND_FAIL);
        }
    }

    public void sendSimpleMailMessage(EmailMessage emailMessage) {
        if (Objects.isNull(emailMessage)) {
            throw new GlobalServiceException("email不能为空", GlobalServiceStatusCode.PARAM_IS_BLANK);
        }
        // 封装simpleMailMessage对象
        SimpleMailMessage simpleMailMessage = emailToSimpleMailMessage(emailMessage);
        // 发送
        javaMailSender.send(simpleMailMessage);
    }

    public void sendMailWithFile(EmailMessage emailMessage, File... files) {
        if (Objects.isNull(emailMessage)) {
            throw new GlobalServiceException("email不能为空", GlobalServiceStatusCode.PARAM_IS_BLANK);
        }
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

    public void sendModelMail(EmailMessage emailMessage, String template, Object modelMessage) {
        if (Objects.isNull(emailMessage)) {
            throw new GlobalServiceException("email不能为空", GlobalServiceStatusCode.PARAM_IS_BLANK);
        }
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

    public void sendModelMailWithFile(EmailMessage emailMessage, String template, Object modelMessage, File... files) {
        if (Objects.isNull(emailMessage)) {
            throw new GlobalServiceException("email不能为空", GlobalServiceStatusCode.PARAM_IS_BLANK);
        }
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

    public <T, R> void customizedSendEmail(EmailMessage emailMessage, String template, Function<T, R> function, File... files) {
        if (Objects.isNull(emailMessage)) {
            throw new GlobalServiceException("email不能为空", GlobalServiceStatusCode.PARAM_IS_BLANK);
        }
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
                        Object modelMessage = function.apply((T) s);
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
