package com.achobeta.email.factory;

import cn.hutool.core.util.RandomUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.exception.GlobalServiceException;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-13
 * Time: 15:22
 */
@Configuration
@Setter
@ConfigurationProperties("ab.mail")
public class EmailSenderFactory implements InitializingBean {

    private List<EmailSenderProperties> senders;

    private List<JavaMailSenderImpl> senderList;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.senderList = new ArrayList<>();
        Optional.ofNullable(senders).stream().flatMap(List::stream).forEach(sender -> {
            // 邮件发送者
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost(sender.getHost());
            javaMailSender.setPort(sender.getPort());
            javaMailSender.setUsername(sender.getUsername());
            javaMailSender.setPassword(sender.getPassword());
            javaMailSender.setProtocol(sender.getProtocol());
            javaMailSender.setDefaultEncoding(sender.getDefaultEncoding());
            javaMailSender.setJavaMailProperties(sender.getProperties());
            senderList.add(javaMailSender);
        });
        if(CollectionUtils.isEmpty(this.senderList)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.EMAIL_SENDER_NOT_EXISTS);
        }
    }

    public JavaMailSenderImpl fetch() {
        return senderList.get(RandomUtil.randomInt(senderList.size()));
    }
}
