package com.achobeta.domain.users.service.impl;

import com.achobeta.domain.email.component.EmailSender;
import com.achobeta.domain.email.component.po.Email;
import com.achobeta.domain.users.model.vo.EmailModelMessage;
import com.achobeta.domain.users.repository.EmailRepository;
import com.achobeta.domain.users.service.EmailService;
import com.achobeta.domain.users.util.IdentifyingCodeValidator;
import com.achobeta.exception.EmailIdentifyingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {


    private final EmailSender emailSender;

    private final EmailRepository emailRepository;



    @Value("${spring.mail.username}")
    private String achobetaEmail;


    private static final int IDENTIFYING_CODE_MINUTES = 5;//过期分钟数

    private static final int IDENTIFYING_CODE_TIMEOUT = IDENTIFYING_CODE_MINUTES * 60  * 1000; //单位为毫秒

    private static final String EMAIL_MODEL_HTML = "identifying-code-model.html";

    @Override
    public void sendIdentifyingCode(String email, String code) {
        // 封装 Email
        Email emailMessage = new Email();
        emailMessage.setContent(code);
        emailMessage.setCreateTime(new Date());
        emailMessage.setTitle(IdentifyingCodeValidator.IDENTIFYING_CODE_PURPOSE);
        emailMessage.setRecipient(email);
        emailMessage.setCarbonCopy();
        emailMessage.setSender(achobetaEmail);
        // 存到 redis 中
        String redisKey = IdentifyingCodeValidator.REDIS_EMAIL_IDENTIFYING_CODE + email;
        emailRepository.setIdentifyingCode(redisKey, code, IDENTIFYING_CODE_TIMEOUT);
        // 构造模板消息
        EmailModelMessage emailModelMessage = new EmailModelMessage();
        emailModelMessage.setCode(code);
        emailModelMessage.setMinutes(IDENTIFYING_CODE_MINUTES);
        // 发送模板消息
        emailSender.sendModelMail(emailMessage, EMAIL_MODEL_HTML, emailModelMessage);
    }

    @Override
    public void checkIdentifyingCode(String redisKey, String code) {
        Object data = emailRepository.getIdentifyingCode(redisKey).orElseGet(() -> {
            throw new EmailIdentifyingException("邮箱不存在/用户未获取验证码/验证码过期/用户已验证");
        });
        // 取出验证码和过期时间点
        Map<String, Object> map = (Map<String, Object>) data;
        String codeValue = (String) map.get(IdentifyingCodeValidator.IDENTIFYING_CODE);
        long deadline = (long) map.get(IdentifyingCodeValidator.IDENTIFYING_DEADLINE);
        // 验证是否过期
        if(System.currentTimeMillis() > deadline) {
            throw new EmailIdentifyingException("验证码过期");
        }
        // 验证是否正确
        if(!codeValue.equals(code)) {
            throw new EmailIdentifyingException("验证码错误");
        }
        // 验证成功
        emailRepository.deleteIdentifyingCodeRecord(redisKey);
        // todo: 进行一些其他的业务
    }
}
