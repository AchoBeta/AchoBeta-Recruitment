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
        String redisKey = IdentifyingCodeValidator.REDIS_EMAIL_IDENTIFYING_CODE + email;
        // 验证一下一分钟以内发过了没有
        emailRepository.getIdentifyingCode(redisKey).ifPresent((data) -> {
            if (!IdentifyingCodeValidator.isAllowedToSend((Map<String, Object>) data,
                    IDENTIFYING_CODE_INTERVAL_Limit, IDENTIFYING_CODE_TIMEOUT)) {
                throw new GlobalServiceException("短时间内多次申请验证码", GlobalServiceStatusCode.EMAIL_SEND_FAIL);
            }
        });
        // 封装 Email
        Email emailMessage = new Email();
        emailMessage.setContent(code);
        emailMessage.setCreateTime(new Date());
        emailMessage.setTitle(IdentifyingCodeValidator.IDENTIFYING_CODE_PURPOSE);
        emailMessage.setRecipient(email);
        emailMessage.setCarbonCopy();
        emailMessage.setSender(achobetaEmail);
        // 存到 redis 中
        redisKey = IdentifyingCodeValidator.REDIS_EMAIL_IDENTIFYING_CODE + email;
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
        long deadline = (long) map.get(IdentifyingCodeValidator.IDENTIFYING_DEADLINE);
        int opportunities = (int) map.get(IdentifyingCodeValidator.IDENTIFYING_OPPORTUNITIES);
        // 验证是否过期
        if (System.currentTimeMillis() > deadline) {
            throw new GlobalServiceException(GlobalServiceStatusCode.EMAIL_CODE_EXPIRE);
        }
        // 还有没有验证机会
        if (opportunities < 1) {
            throw new GlobalServiceException(GlobalServiceStatusCode.EMAIL_CODE_OPPORTUNITIES_EXHAUST);
        }
        // 验证是否正确
        if (!codeValue.equals(code)) {
            // 计算新的超时时间，或者其实也可以继续设置五分钟，防止有deadline
            long timeout = Math.max(0, deadline - System.currentTimeMillis());
            // 次数减一
            map.put(IdentifyingCodeValidator.IDENTIFYING_OPPORTUNITIES, opportunities - 1);
            emailRepository.setIdentifyingCode(redisKey, map, timeout);
            throw new GlobalServiceException(GlobalServiceStatusCode.EMAIL_CODE_NOT_CONSISTENT);
        }
        // 验证成功
        emailRepository.deleteIdentifyingCodeRecord(redisKey);
        // todo: 进行一些其他的业务
    }
}
