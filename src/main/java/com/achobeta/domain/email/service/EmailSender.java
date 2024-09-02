package com.achobeta.domain.email.service;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.email.model.po.EmailMessage;
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

    private final EmailHtmlEngine emailHtmlEngine;

    private SimpleMailMessage emailToSimpleMailMessage(@NonNull EmailMessage emailMessage) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailMessage.getSender());
        simpleMailMessage.setTo(emailMessage.getRecipient());
        simpleMailMessage.setCc(emailMessage.getCarbonCopy());
        simpleMailMessage.setSentDate(emailMessage.getCreateTime());
        simpleMailMessage.setSubject(emailMessage.getTitle());
        simpleMailMessage.setText(emailMessage.getContent());
        return simpleMailMessage;
    }

    private MimeMessageHelper emailIntoMimeMessageByHelper(MimeMessage mimeMessage, @NonNull EmailMessage emailMessage) throws MessagingException {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, Boolean.TRUE);
        mimeMessageHelper.setFrom(emailMessage.getSender());
        mimeMessageHelper.setCc(emailMessage.getCarbonCopy());
        mimeMessageHelper.setSubject(emailMessage.getTitle());
        mimeMessageHelper.setSentDate(emailMessage.getCreateTime());
        mimeMessageHelper.setTo(emailMessage.getRecipient());
        return mimeMessageHelper;
    }

    private String buildEmailHtml(String template, Object data) {
        // 构造 html
        return emailHtmlEngine.builder()
                .append(template, data)
                .build();
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
            mimeMessageHelper.setText(emailMessage.getContent(), Boolean.FALSE);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.EMAIL_SEND_FAIL);
        }
    }

    public void sendModelMail(@NonNull EmailMessage emailMessage, String html) {
        try {
            // 封装对象
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = emailIntoMimeMessageByHelper(mimeMessage, emailMessage);
            mimeMessageHelper.setText(html, Boolean.TRUE);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.EMAIL_SEND_FAIL);
        }
    }

    public void sendModelMail(@NonNull EmailMessage emailMessage, String template, Object modelMessage) {
        sendModelMail(emailMessage, buildEmailHtml(template, modelMessage));
    }

    public void sendModelMailWithFile(@NonNull EmailMessage emailMessage, String template, Object modelMessage, File... files) {
        // 封装对象
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = emailIntoMimeMessageByHelper(mimeMessage, emailMessage);
            mimeMessageHelper.setText(buildEmailHtml(template, modelMessage), Boolean.TRUE);
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

    public void customizedSendEmail(@NonNull EmailMessage emailMessage, String template, Function<String, ?> function, File... files) {
        String sender = emailMessage.getSender();
        String[] carbonCopy = emailMessage.getCarbonCopy();
        String title = emailMessage.getTitle();
        Arrays.stream(emailMessage.getRecipient())
                .parallel()
                .distinct()
                .forEach(to -> {
                    try {
                        // 封装对象
                        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, Boolean.TRUE);
                        mimeMessageHelper.setTo(to);
                        mimeMessageHelper.setFrom(sender);
                        mimeMessageHelper.setCc(carbonCopy);
                        mimeMessageHelper.setSubject(title);
                        // 添加附件
                        for (File file : files) {
                            if (Objects.nonNull(file)) {
                                mimeMessageHelper.addAttachment(file.getName(), file);
                            }
                        }
                        // 通过mimeMessageHelper设置到mimeMessage里
                        mimeMessageHelper.setText(buildEmailHtml(template, function.apply(to)), Boolean.TRUE);
                        //发送
                        javaMailSender.send(mimeMessage);
                    } catch (MessagingException e) {
                        throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.EMAIL_SEND_FAIL);
                    }
                });
    }

}
