package com.achobeta.domain.message.handler.ext;

import com.achobeta.domain.message.handler.MessageSendHandler;
import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.model.dto.MessageSendDTO;
import com.achobeta.domain.message.model.entity.AttachmentFile;
import com.achobeta.domain.message.model.vo.EmailMessageSendTemplate;
import com.achobeta.domain.resource.service.ResourceService;
import com.achobeta.email.model.po.EmailAttachment;
import com.achobeta.email.model.po.EmailMessage;
import com.achobeta.email.sender.EmailSender;
import com.achobeta.template.engine.HtmlEngine;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.achobeta.email.enums.EmailTemplateEnum.MESSAGE_EMAIL_NOTICE;


@Component("email" + MessageSendHandler.HANDLER_BASE_NAME)
@RequiredArgsConstructor
public class MessageSendWithEmailHandler extends MessageSendHandler {

    @Value("${spring.mail.username}")
    private String achobetaEmail;

    private final ResourceService resourceService;
    private final EmailSender emailSender;
    private final HtmlEngine htmlEngine;
    private final HttpServletRequest request;

    @Override
    public void handle(MessageSendDTO messageSendBody, CopyOnWriteArraySet<MessageReceiveServer> webSocketSet) {


        messageSendBody.getMessageContent().getStuInfoSendList().stream().forEach(stuInfo -> {

            sendEmail(stuInfo.getEmail(), messageSendBody.getMessageContent().getTittle(),
                    messageSendBody.getMessageContent().getContent(), stuInfo.getStuName(),null);

        });
        super.doNextHandler(messageSendBody, webSocketSet);
    }


    public void sendEmail(String email, String tittle, String content, String stuName, List<AttachmentFile> attachmentInfoList) {
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
                .attachmentInfoList(attachmentInfoList)
                .build();
        
        
        String html = htmlEngine.builder()
                .append(MESSAGE_EMAIL_NOTICE.getTemplate(), emailMessageSendTemplate)
                .build();
        emailMessage.setContent(html);
        // 发送模板消息
        emailSender.send(emailMessage);
    }

    public void sendEmail(String email, String tittle, String content, String stuName, List<AttachmentFile> attachmentInfoList, List<MultipartFile> multipartFileList) {
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
                .attachmentInfoList(attachmentInfoList)
                .build();


        String html = htmlEngine.builder()
                .append(MESSAGE_EMAIL_NOTICE.getTemplate(), emailMessageSendTemplate)
                .build();
        emailMessage.setContent(html);

        List<EmailAttachment> emailAttachmentList = Collections.emptyList();
        if(multipartFileList!=null&&!multipartFileList.isEmpty()){
            //构造邮箱附件列表
            emailAttachmentList = multipartFileList.stream().map(file -> {
                    return EmailAttachment.of(file);
            }).toList();
        }

        // 发送模板消息
        emailSender.send(emailMessage,true,emailAttachmentList);
    }
}
