package com.achobeta.email.provider;

import cn.hutool.extra.spring.SpringUtil;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.email.config.EmailSenderConfig;
import com.achobeta.email.provider.strategy.ProvideStrategy;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-13
 * Time: 22:50
 */
@Component
@RequiredArgsConstructor
public class EmailSenderProvider implements InitializingBean {

    private final EmailSenderConfig emailSenderConfig;

    private List<JavaMailSenderImpl> senderList;

    private ProvideStrategy provideStrategy;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 构造邮件发送器实现
        this.senderList = ObjectUtil.distinctNonNullStream(emailSenderConfig.getSenders()).map(sender -> {
            // 邮件发送者
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost(sender.getHost());
            javaMailSender.setPort(sender.getPort());
            javaMailSender.setUsername(sender.getUsername());
            javaMailSender.setPassword(sender.getPassword());
            javaMailSender.setProtocol(sender.getProtocol());
            javaMailSender.setDefaultEncoding(sender.getDefaultEncoding());
            javaMailSender.setJavaMailProperties(sender.getProperties());
            return javaMailSender;
        }).toList();
        // 若不存在一个实现则抛出异常（启动项目时）
        if(CollectionUtils.isEmpty(this.senderList)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.EMAIL_SENDER_NOT_EXISTS);
        }
        // 初始化获取发送器实现的策略
        this.provideStrategy = SpringUtil.getBean(
                emailSenderConfig.getStrategy() + ProvideStrategy.BASE_NAME,
                ProvideStrategy.class
        );
    }

    public JavaMailSenderImpl provide() {
        return provideStrategy.getSender(senderList);
    }

}
