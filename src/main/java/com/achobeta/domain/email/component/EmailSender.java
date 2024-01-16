package com.achobeta.domain.email.component;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.domain.email.component.po.Email;
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

    public SimpleMailMessage emailToSimpleMailMessage(Email email) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(email.getSender());
        simpleMailMessage.setTo(email.getRecipient());
        simpleMailMessage.setCc(email.getCarbonCopy());
        simpleMailMessage.setSubject(email.getTitle());
        simpleMailMessage.setText(email.getContent());
        return simpleMailMessage;
    }


    public MimeMessageHelper emailIntoMimeMessageByHelper(MimeMessage mimeMessage, Email email) {
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(email.getSender());
            mimeMessageHelper.setCc(email.getCarbonCopy());
            mimeMessageHelper.setSubject(email.getTitle());
            mimeMessageHelper.setTo(email.getRecipient());
            return mimeMessageHelper;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendSimpleMailMessage(Email email) {
        if(Objects.isNull(email)) {
            throw new RuntimeException("email不能为null!");
        }
        // 封装simpleMailMessage对象
        SimpleMailMessage simpleMailMessage = emailToSimpleMailMessage(email);
        // 发送
        javaMailSender.send(simpleMailMessage);
    }


    public void sendMailWithFile(Email email, File... files) {
        if(Objects.isNull(email)) {
            throw new RuntimeException("email不能为null!");
        }
        // 封装对象
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = emailIntoMimeMessageByHelper(mimeMessage, email);
            // 添加附件
            for(File file : files) {
                if(Objects.nonNull(file)) {
                    mimeMessageHelper.addAttachment(file.getName(), file);
                }
            }
            mimeMessageHelper.setText(email.getContent(), false);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendModelMail(Email email, String template, Object modelMessage) {
        if(Objects.isNull(email)) {
            throw new RuntimeException("email不能为null!");
        }
        // 封装对象
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = emailIntoMimeMessageByHelper(mimeMessage, email);
            // 构造模板消息
            Context context = new Context();
            context.setVariables(BeanUtil.beanToMap(modelMessage));
            //合并模板与数据
            String content = templateEngine.process(template, context);
            mimeMessageHelper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendModelMailWithFile(Email email, String template, Object modelMessage, File... files) {
        if(Objects.isNull(email)) {
            throw new RuntimeException("email不能为null!");
        }
        // 封装对象
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = emailIntoMimeMessageByHelper(mimeMessage, email);
            // 构造模板消息
            Context context = new Context();
            context.setVariables(BeanUtil.beanToMap(modelMessage));
            //合并模板与数据
            String content = templateEngine.process(template, context);
            mimeMessageHelper.setText(content, true);
            // 添加附件
            for(File file : files) {
                if(Objects.nonNull(file)) {
                    mimeMessageHelper.addAttachment(file.getName(), file);
                }
            }
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T, R> void customizedSendEmail(Email email, String template, Function<T, R> function, File... files) {
        if(Objects.isNull(email)) {
            throw new RuntimeException("email不能为null!");
        }
        String sender = email.getSender();
        String[] carbonCopy = email.getCarbonCopy();
        String title = email.getTitle();
        Arrays.stream(email.getRecipient())
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
                        for(File file : files) {
                            if(Objects.nonNull(file)) {
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
                        throw new RuntimeException(e);
                    }
                });
    }
}