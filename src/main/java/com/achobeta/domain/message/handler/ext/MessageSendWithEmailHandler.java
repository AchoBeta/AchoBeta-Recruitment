package com.achobeta.domain.message.handler.ext;

import com.achobeta.domain.email.model.vo.VerificationCodeTemplate;
import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.handler.MessageSendHandler;
import com.achobeta.domain.message.model.dto.MessageSendDTO;
import com.achobeta.domain.message.model.vo.EmailMessageSendTemplate;
import com.achobeta.email.model.po.EmailMessage;
import com.achobeta.email.sender.EmailSender;
import com.achobeta.template.engine.HtmlEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.achobeta.common.enums.EmailTemplateEnum.CAPTCHA;
import static com.achobeta.common.enums.EmailTemplateEnum.MESSAGE_EMAIL_NOTICE;


@Component("email" + MessageSendHandler.HANDLER_BASE_NAME)
@RequiredArgsConstructor
public class MessageSendWithEmailHandler extends MessageSendHandler {

    @Value("${spring.mail.username}")
    private String achobetaEmail;

    private final EmailSender emailSender;
    private final HtmlEngine htmlEngine;

    @Override
    public void handle(MessageSendDTO messageSendBody, CopyOnWriteArraySet<MessageReceiveServer> webSocketSet) {


        messageSendBody.getMessageContent().getStuInfoSendList().stream().forEach(stuInfo -> {

            buildEmailAndSend(stuInfo.getEmail(), messageSendBody.getMessageContent().getTittle(),
                    messageSendBody.getMessageContent().getContent(), stuInfo.getStuName());

        });
        super.doNextHandler(messageSendBody, webSocketSet);
    }

    public void buildEmailAndSend(String email, String tittle, String content, String stuName) {
        // 封装 Email
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setCreateTime(new Date());
        emailMessage.setTitle(MESSAGE_EMAIL_NOTICE.getTitle());
        emailMessage.setRecipient(email);
        emailMessage.setCarbonCopy();
        emailMessage.setSender(achobetaEmail);
        //构造当前时间
        LocalDateTime nowTime = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = nowTime.format(timeFormatter);
        // 构造模板消息
        EmailMessageSendTemplate emailMessageSendTemplate = EmailMessageSendTemplate.builder()
                .stuName(stuName)
                .tittle(tittle)
                .content(content)
                .sendTime(now)
                .build();


        String html = htmlEngine.builder()
                .append(MESSAGE_EMAIL_NOTICE.getTemplate(), emailMessageSendTemplate)
                .build();
        emailMessage.setContent(html);
        // 发送模板消息
        emailSender.send(emailMessage);
    }
}
