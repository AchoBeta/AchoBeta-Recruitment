package com.achobeta.domain.message.handler.ext;

import com.achobeta.domain.message.handler.MessageSendHandler;
import com.achobeta.domain.message.handler.websocket.MessageReceiveServer;
import com.achobeta.domain.message.model.dto.MessageSendDTO;
import com.achobeta.domain.message.model.entity.AttachmentFile;
import com.achobeta.domain.message.model.vo.EmailMessageSendTemplate;
import com.achobeta.email.model.po.EmailAttachment;
import com.achobeta.email.model.po.EmailMessage;
import com.achobeta.email.sender.EmailSender;
import com.achobeta.template.engine.HtmlEngine;
import com.achobeta.template.util.TemplateUtil;
import com.achobeta.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    private final EmailSender emailSender;
    private final HtmlEngine htmlEngine;

    @Override
    public void handle(MessageSendDTO messageSendBody, CopyOnWriteArraySet<MessageReceiveServer> webSocketSet) {


        messageSendBody.getMessageContent().getStuInfoSendList().stream().forEach(stuInfo -> {

            sendEmail(stuInfo.getEmail(), messageSendBody.getMessageContent().getTittle(),
                    messageSendBody.getMessageContent().getContent(), stuInfo.getStuName(),null);

        });
        super.doNextHandler(messageSendBody, webSocketSet);
    }

    private EmailMessage getNoticeMessage(String email, String tittle, String content, String stuName, List<AttachmentFile> attachmentInfoList) {
        // 封装 Email
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setCreateTime(new Date());
        emailMessage.setTitle(MESSAGE_EMAIL_NOTICE.getTitle());
        emailMessage.setRecipient(email);
        emailMessage.setCarbonCopy();
        emailMessage.setSender(achobetaEmail);

        //构造当前时间
        String now = TimeUtil.getDateTime(new Date());

        // 构造模板消息
        String contentTarget = TemplateUtil.getUniqueSymbol();
        EmailMessageSendTemplate emailMessageSendTemplate = EmailMessageSendTemplate.builder()
                .stuName(stuName)
                .tittle(tittle)
                .content(contentTarget)
                .sendTime(now)
                .attachmentInfoList(attachmentInfoList)
                .build();

        String html = htmlEngine.builder()
                .append(MESSAGE_EMAIL_NOTICE.getTemplate(), emailMessageSendTemplate)
                .replaceMarkdown(contentTarget, content)
                .build();
        emailMessage.setContent(html);
        return emailMessage;
    }

    public void sendEmail(String email, String tittle, String content, String stuName, List<AttachmentFile> attachmentInfoList, List<MultipartFile> multipartFileList) {
        // 封装 Email
        EmailMessage emailMessage = getNoticeMessage(email, tittle, content, stuName, attachmentInfoList);

        List<EmailAttachment> emailAttachmentList = Collections.emptyList();
        if(!CollectionUtils.isEmpty(multipartFileList)){
            //构造邮箱附件列表
            emailAttachmentList = multipartFileList.stream().map(EmailAttachment::of).toList();
        }

        // 发送模板消息
        emailSender.send(emailMessage,true,emailAttachmentList);
    }

    public void sendEmail(String email, String tittle, String content, String stuName, List<AttachmentFile> attachmentInfoList) {
        sendEmail(email, tittle, content, stuName, attachmentInfoList, new ArrayList<>());
    }
}
