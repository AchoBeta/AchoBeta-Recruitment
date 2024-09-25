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
    MESSAGE("用户面试消息通知", "identifying-code-model.html"),

    INTERVIEW_NOTICE("面试通知", "interview-notice-model.html"),

    INTERVIEW_SUMMARY("面试总结", "interview-summary-model.html"),

    INTERVIEW_SUMMARY_MARKDOWN("面试总结", "interview-summary-model.md"),

    INTERVIEW_EXPERIENCE_OPEN("面试经历", "interview-experience-open.html"),

    INTERVIEW_EXPERIENCE_INNER("面试经历", "interview-experience-inner.html"),

    INTERVIEW_EXPERIENCE_CLOSE("面试经历", "interview-experience-close.html"),

    RESUME_NOTICE("简历状态轮转通知", "resume-notice-model.html"),

    MEMBER_NOTICE("转正通知", "confirmation-notice-model.html"),

    MESSAGE_EMAIL_NOTICE("Achobeta消息通知", "message-email-notice.html"),

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
