package com.achobeta.email.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author BanTanger 半糖
 * @date 2024/1/23 9:27
 */
@Getter
@AllArgsConstructor
public enum EmailTemplateEnum {

    CAPTCHA("AchoBeta 邮箱验证：验证用户身份", "identifying-code-model.html"),

    INTERVIEW_NOTICE("AchoBeta 面试通知", "interview-notice-model.html"),

    INTERVIEW_SUMMARY("AchoBeta 面试总结", "interview-summary-model.html"),

    INTERVIEW_SUMMARY_MARKDOWN("AchoBeta 面试总结", "interview-summary-model.md"),

    INTERVIEW_EXPERIENCE("AchoBeta 面试经历", "interview-experience-model.html"),

    RESUME_NOTICE("AchoBeta 简历状态轮转通知", "resume-notice-model.html"),

    MEMBER_NOTICE("AchoBeta 转正通知", "confirmation-notice-model.html"),

    MESSAGE_EMAIL_NOTICE("AchoBeta 消息通知", "message-email-notice.html"),

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
