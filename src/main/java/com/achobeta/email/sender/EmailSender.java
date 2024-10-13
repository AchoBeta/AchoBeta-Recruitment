package com.achobeta.email.sender;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.email.factory.EmailSenderFactory;
import com.achobeta.email.model.po.EmailAttachment;
import com.achobeta.email.model.po.EmailMessage;
import com.achobeta.exception.GlobalServiceException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {

    private final EmailSenderFactory emailSenderFactory;

    public void send(String sender, String[] recipient, String[] carbonCopy,
                     String title, String content, boolean isHtml,
                     Date sentDate, List<EmailAttachment> fileList) {
        // 封装对象
        try {
            // 获取邮件发送器实现
            JavaMailSenderImpl javaMailSender = emailSenderFactory.fetch();
            // 构造邮件
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, Boolean.TRUE);
            // 确定发送至邮箱地址
            String from = Optional.ofNullable(sender)
                    .filter(StringUtils::hasText)
                    .or(() -> Optional.ofNullable(javaMailSender.getUsername()))
                    .filter(StringUtils::hasText)
                    .orElseThrow(() -> new GlobalServiceException("无法确定发送者邮箱地址", GlobalServiceStatusCode.EMAIL_SEND_FAIL));
            mimeMessageHelper.setFrom(from);
            // 设置标题与内容
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(content, isHtml);
            // 设置发送时间（但大部分邮箱不支持此功能）
            mimeMessageHelper.setSentDate(Optional.ofNullable(sentDate).orElseGet(Date::new));
            // 设置接受者邮箱地址
            mimeMessageHelper.setTo(recipient);
            // 指定抄送人，避免一些情况导致发送邮件失败
            mimeMessageHelper.setCc(Optional.ofNullable(carbonCopy).filter(cc -> cc.length > 0).orElse(recipient));
            // 设置附件
            for (EmailAttachment attachment : Optional.ofNullable(fileList).orElseGet(ArrayList::new)) {
                if (Objects.nonNull(attachment)) {
                    mimeMessageHelper.addAttachment(attachment.getFileName(), attachment);
                }
            }
            // 发送
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.EMAIL_SEND_FAIL);
        }
    }

    public void send(EmailMessage emailMessage, boolean isHtml, List<EmailAttachment> files) {
        // 封装对象
        send(emailMessage.getSender(), emailMessage.getRecipient(), emailMessage.getCarbonCopy(),
                emailMessage.getTitle(), emailMessage.getContent(), isHtml,
                emailMessage.getCreateTime(), files);
    }

    public void send(EmailMessage emailMessage, boolean isHtml) {
        send(emailMessage, isHtml, new ArrayList<>());
    }

    public void send(EmailMessage emailMessage) {
        send(emailMessage, Boolean.TRUE);
    }

    /**
     * 不建议使用，因为通过 email 可能不足以获得我们需要的文本
     * 建议循环调用 send，因为这个方法本身就是循环发送，不是一次性发送
     */
    @Deprecated
    public void customSend(EmailMessage emailMessage, Function<String, String> getText, boolean isHtml, List<EmailAttachment> fileList) {
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