package com.achobeta.domain.users.service.impl;

import com.achobeta.common.constants.GlobalServiceStatusCode;
import com.achobeta.domain.email.component.EmailSender;
import com.achobeta.domain.email.component.po.Email;
import com.achobeta.domain.users.model.vo.VerificationCodeTemplate;
import com.achobeta.domain.users.repository.EmailRepository;
import com.achobeta.domain.users.service.EmailService;
import com.achobeta.domain.users.util.IdentifyingCodeValidator;
import com.achobeta.exception.GlobalServiceException;
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

    private static final int IDENTIFYING_CODE_MINUTES = 5;//过期分钟数

    private static final long IDENTIFYING_CODE_INTERVAL_Limit = 1 * 60 * 1000; // 两次发送验证码的最短时间间隔

    private static final long IDENTIFYING_CODE_TIMEOUT = IDENTIFYING_CODE_MINUTES * 60 * 1000; //单位为毫秒

    private static final int IDENTIFYING_OPPORTUNITIES_LIMIT = 5; // 只有五次验证机会

    private static final String EMAIL_MODEL_HTML = "identifying-code-model.html"; // Email 验证码通知 -模板

    @Value("${spring.mail.username}")
    private String achobetaEmail;

    private final EmailSender emailSender;

    private final EmailRepository emailRepository;

    @Override
    public void sendIdentifyingCode(String email, String code) {
        final String redisKey = IdentifyingCodeValidator.REDIS_EMAIL_IDENTIFYING_CODE + email;
        // 验证一下一分钟以内发过了没有
        long ttl = emailRepository.getTTLOfCode(redisKey); // 小于 0 则代表没有到期时间或者不存在，允许发送
        if(ttl > IDENTIFYING_CODE_TIMEOUT - IDENTIFYING_CODE_INTERVAL_Limit) {
            String message = String.format("请在 %d 分钟后再重新申请", IDENTIFYING_CODE_INTERVAL_Limit / (60 * 1000L));
            throw new GlobalServiceException(message, GlobalServiceStatusCode.EMAIL_SEND_FAIL);
        }
        // 封装 Email
        Email emailMessage = new Email();
        emailMessage.setContent(code);
        emailMessage.setCreateTime(new Date());
        emailMessage.setTitle(IdentifyingCodeValidator.IDENTIFYING_CODE_PURPOSE);
        emailMessage.setRecipient(email);
        emailMessage.setCarbonCopy();
        emailMessage.setSender(achobetaEmail);
        // 存到 redis 中
        emailRepository.setIdentifyingCode(redisKey, code, IDENTIFYING_CODE_TIMEOUT, IDENTIFYING_OPPORTUNITIES_LIMIT);
        // 构造模板消息
        VerificationCodeTemplate verificationCodeTemplate = VerificationCodeTemplate.builder()
                .code(code)
                .minutes(IDENTIFYING_CODE_MINUTES)
                .build();
        // 发送模板消息
        emailSender.sendModelMail(emailMessage, EMAIL_MODEL_HTML, verificationCodeTemplate);
    }

    @Override
    public void checkIdentifyingCode(String email, String code) {
        String redisKey = IdentifyingCodeValidator.REDIS_EMAIL_IDENTIFYING_CODE + email;
        Object data = null;
        data = emailRepository.getIdentifyingCode(redisKey).orElseThrow(() -> {
            String message = String.format("Redis 中不存在邮箱[%s]的相关记录", email);
            return new GlobalServiceException(message, GlobalServiceStatusCode.EMAIL_NOT_EXIST_RECORD);
        });
        // 取出验证码和过期时间点
        Map<String, Object> map = (Map<String, Object>) data;
        String codeValue = (String) map.get(IdentifyingCodeValidator.IDENTIFYING_CODE);
        int opportunities = (int) map.get(IdentifyingCodeValidator.IDENTIFYING_OPPORTUNITIES);
        // 还有没有验证机会
        if (opportunities < 1) {
            throw new GlobalServiceException(GlobalServiceStatusCode.EMAIL_CODE_OPPORTUNITIES_EXHAUST);
        }
        // 验证是否正确
        if (!codeValue.equals(code)) {
            // 次数减一
            opportunities = (int)emailRepository.decrementOpportunities(redisKey);
            String message = String.format("验证码错误，剩余%d次机会", opportunities);
            throw new GlobalServiceException(message, GlobalServiceStatusCode.EMAIL_CODE_NOT_CONSISTENT);
        }
        // 验证成功
        emailRepository.deleteIdentifyingCodeRecord(redisKey);
    }
}
