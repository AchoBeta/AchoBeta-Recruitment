package com.achobeta.email;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.email.model.po.EmailMessage;
import com.achobeta.exception.GlobalServiceException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection"})
public class EmailSender {

    private final JavaMailSender javaMailSender;

    private SimpleMailMessage emailToSimpleMailMessage(EmailMessage emailMessage) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailMessage.getSender());
        simpleMailMessage.setTo(emailMessage.getRecipient());
        simpleMailMessage.setCc(emailMessage.getCarbonCopy());
        simpleMailMessage.setSentDate(emailMessage.getCreateTime());
        simpleMailMessage.setSubject(emailMessage.getTitle());
        simpleMailMessage.setText(emailMessage.getContent());
        return simpleMailMessage;
    }

    private MimeMessageHelper emailIntoMimeMessageByHelper(MimeMessage mimeMessage, EmailMessage emailMessage) throws MessagingException {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, Boolean.TRUE);
        mimeMessageHelper.setFrom(emailMessage.getSender());
        mimeMessageHelper.setCc(emailMessage.getCarbonCopy());
        mimeMessageHelper.setSubject(emailMessage.getTitle());
        mimeMessageHelper.setSentDate(emailMessage.getCreateTime());
        mimeMessageHelper.setTo(emailMessage.getRecipient());
        return mimeMessageHelper;
    }

    public void sendSimpleMailMessage(EmailMessage emailMessage) {
        // 封装simpleMailMessage对象
        SimpleMailMessage simpleMailMessage = emailToSimpleMailMessage(emailMessage);
        // 发送
        javaMailSender.send(simpleMailMessage);
    }

    public void sendMailWithFile(EmailMessage emailMessage, File... files) {
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

    public void sendModelMail(EmailMessage emailMessage, String html) {
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

    public void sendModelMailWithFile(EmailMessage emailMessage, String html, File... files) {
        // 封装对象
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = emailIntoMimeMessageByHelper(mimeMessage, emailMessage);
            mimeMessageHelper.setText(html, Boolean.TRUE);
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

    /**
     * 不建议使用，因为通过 email 可能不足以获得我们需要的 html
     * 建议循环调用 sendModelMailWithFile，因为这个方法本身就是循环发送，不是一次性发送
     */
    @Deprecated
    public void customizedSendEmail(EmailMessage emailMessage, Function<String, String> getHtml, File... files) {
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
                        mimeMessageHelper.setText(getHtml.apply(to), Boolean.TRUE);
                        //发送
                        javaMailSender.send(mimeMessage);
                    } catch (MessagingException e) {
                        throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.EMAIL_SEND_FAIL);
                    }
                });
    }

}