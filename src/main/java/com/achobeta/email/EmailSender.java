package com.achobeta.email;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.email.model.po.EmailMessage;
import com.achobeta.exception.GlobalServiceException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection"})
public class EmailSender {

    private final JavaMailSender javaMailSender;

    public void send(String sender, String[] recipient, String[] carbonCopy,
                     String title, String content, boolean isHtml,
                     Date sentDate, List<File> fileList) {
        // 封装对象
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, Boolean.TRUE);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setCc(carbonCopy);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setSentDate(sentDate);
            mimeMessageHelper.setTo(recipient);
            // 设置文本
            mimeMessageHelper.setText(content, isHtml);
            // 设置附件
            for (File file : fileList) {
                if (Objects.nonNull(file)) {
                    mimeMessageHelper.addAttachment(file.getName(), file);
                }
            }
            // 发送
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.EMAIL_SEND_FAIL);
        }
    }

    public void send(EmailMessage emailMessage, boolean isHtml, List<File> files) {
        // 封装对象
        send(emailMessage.getSender(), emailMessage.getRecipient(), emailMessage.getCarbonCopy(),
                emailMessage.getTitle(), emailMessage.getContent(), isHtml,
                emailMessage.getCreateTime(), files);
    }

    public void send(EmailMessage emailMessage, boolean isHtml) {
        send(emailMessage, isHtml, Collections.emptyList());
    }

    public void send(EmailMessage emailMessage) {
        send(emailMessage, Boolean.TRUE, Collections.emptyList());
    }

    /**
     * 不建议使用，因为通过 email 可能不足以获得我们需要的文本
     * 建议循环调用 send，因为这个方法本身就是循环发送，不是一次性发送
     */
    @Deprecated
    public void customizedSend(EmailMessage emailMessage, Function<String, String> getText, boolean isHtml, List<File> fileList) {
        String sender = emailMessage.getSender();
        String[] carbonCopy = emailMessage.getCarbonCopy();
        String title = emailMessage.getTitle();
        Date sendDate = emailMessage.getCreateTime();
        emailMessage.setContent(null); // 忽略原内容
        Arrays.stream(emailMessage.getRecipient())
                .parallel()
                .distinct()
                .forEach(recipient -> {
                    // 怕出现线程安全问题，所以不能直接传 emailMessage
                    send(sender, new String[]{recipient}, carbonCopy, title, getText.apply(recipient), isHtml, sendDate, fileList);
                });
    }

}