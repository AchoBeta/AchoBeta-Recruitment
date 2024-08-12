package com.achobeta.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author BanTanger 半糖
 * @date 2024/1/23 9:27
 */
@Getter
@AllArgsConstructor
public enum EmailTemplateEnum {

    CAPTCHA("邮箱验证码：验证用户身份", "identifying-code-model.html"),

    INTERVIEW_NOTICE("面试通知", "interview-notice-model.html"),

    ;

    /**
     * 本次邮件拟定标题
     */
    private final String title;
    /**
     * 本次邮件格式模板
     */
    private final String template;

}
