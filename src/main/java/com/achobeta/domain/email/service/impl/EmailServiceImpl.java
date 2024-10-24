package com.achobeta.domain.email.service.impl;

import com.achobeta.domain.email.model.vo.VerificationCodeTemplate;
import com.achobeta.domain.email.service.EmailService;
import com.achobeta.email.model.po.EmailMessage;
import com.achobeta.email.sender.EmailSender;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.redis.cache.RedisCache;
import com.achobeta.template.engine.HtmlEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.achobeta.common.enums.GlobalServiceStatusCode.EMAIL_CAPTCHA_CODE_COUNT_EXHAUST;
import static com.achobeta.common.enums.GlobalServiceStatusCode.EMAIL_SEND_FAIL;
import static com.achobeta.domain.email.constants.EmailConstants.*;
import static com.achobeta.email.enums.EmailTemplateEnum.CAPTCHA;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${ab.email.timeout:5}")
    private Integer timeout;

    @Value("${ab.email.rateLimit:1}")
    private Integer rateLimit;

    @Value("${ab.email.maxRetryCount:3}")
    private Integer maxRetryCount;

    @Value("${ab.email.riskControlTime:1}")
    private Integer riskControlTime;

    private final RedisCache redisCache;
    private final EmailSender emailSender;
    private final HtmlEngine htmlEngine;

    @Override
    public void sendIdentifyingCode(String email, String code) {
        final String redisKey = CAPTCHA_CODES_KEY + email;

        // 检查是否为风控用户, 如果是直接跳过
        if (redisCache.getCacheObject(RISK_CONTROLLER_USERS_KEY + email).isPresent()) {
            String message = String.format("邮箱'%s'已被风控,'%d'小时后解封", email, TimeUnit.DAYS.toHours(riskControlTime));
            // 申请验证码次数用尽
            throw new GlobalServiceException(message, EMAIL_CAPTCHA_CODE_COUNT_EXHAUST);
        }

        // TODO 【扩展】：限流注解简化逻辑
        //  @RateLimiter(key = "email", time = 60, count = 1)
        // 验证一下一分钟以内发过了没有
        long ttl = redisCache.getKeyTTL(redisKey); // 小于 0 则代表没有到期时间或者不存在，允许发送
        if (ttl > TimeUnit.MINUTES.toMillis((timeout - rateLimit))) {
            String message = String.format("申请太频繁, 请在'%d'分钟后再重新申请", rateLimit);
            throw new GlobalServiceException(message, EMAIL_SEND_FAIL);
        }

        // 获取缓存
        Optional<Map<String, Object>> cacheOptional = redisCache.getCacheMap(redisKey);
        cacheOptional.ifPresentOrElse(
                cache -> {
                    long curRetryCount = redisCache.decrementCacheMapNumber(redisKey, CAPTCHA_CODE_CNT_KEY);
                    if (curRetryCount <= 0) {
                        redisCache.setCacheObject(RISK_CONTROLLER_USERS_KEY + email, email, riskControlTime, TimeUnit.DAYS);
                        String message = String.format("邮箱[%s]已被风控，'%d'小时后解封", email, TimeUnit.DAYS.toHours(riskControlTime));
                        // 申请验证码次数用尽
                        throw new GlobalServiceException(message, EMAIL_CAPTCHA_CODE_COUNT_EXHAUST);
                    }
                    redisCache.execute(() -> {
                        redisCache.setCacheMapValue(redisKey, CAPTCHA_CODE_KEY, code);
                        redisCache.expire(redisKey, timeout, TimeUnit.MINUTES);
                    });
                },
                // 如果 redis 没有对应 key 值，初始化
                () -> {
                    Map<String, Object> captchaCodeMap = new HashMap<>();
                    captchaCodeMap.put(CAPTCHA_CODE_CNT_KEY, maxRetryCount);
                    captchaCodeMap.put(CAPTCHA_CODE_KEY, code);
                    redisCache.setCacheMap(redisKey, captchaCodeMap, timeout, TimeUnit.MINUTES);
                }
        );
        // 发送模板消息
        buildEmailAndSend(email, code);
    }

    private void buildEmailAndSend(String email, String code) {
        // 封装 Email
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTitle(CAPTCHA.getTitle());
        emailMessage.setRecipient(email);
        // 构造模板消息
        VerificationCodeTemplate verificationCodeTemplate = VerificationCodeTemplate.builder()
                .code(code)
                .timeout(timeout)
                .build();
        String html = htmlEngine.builder()
                .append(CAPTCHA.getTemplate(), verificationCodeTemplate)
                .build();
        emailMessage.setContent(html);
        // 发送模板消息
        emailSender.send(emailMessage);
    }

}
