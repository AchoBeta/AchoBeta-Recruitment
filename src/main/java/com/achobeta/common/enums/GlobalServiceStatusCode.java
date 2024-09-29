package com.achobeta.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <span>
 * <h3> global service status code </h3>
 * Please note that status code definitions are module-specific
 * and do not occupy other business modules when defining them.
 * </span>
 *
 * @author jettcc in 2024/01/11
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum GlobalServiceStatusCode {
    /* 成功, 默认200 */
    SYSTEM_SUCCESS(200, "操作成功"),

    /* 系统错误 500 - 1000 */
    SYSTEM_SERVICE_FAIL(-4396, "操作失败"),
    SYSTEM_SERVICE_ERROR(-500, "系统异常"),
    SYSTEM_TIME_OUT(-1, "请求超时"),

    /* 参数错误：1001～2000 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),
    PARAM_FAILED_VALIDATE(1005, "参数未通过验证"),

    REQUEST_NOT_VALID(1101, "请求无效"),

    /* 用户错误 2001-3000 */
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_ACCOUNT_EXPIRED(2002, "账号已过期"),
    USER_CREDENTIALS_ERROR(2003, "密码错误"),
    USER_CREDENTIALS_EXPIRED(2004, "密码过期"),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"),
    USER_ACCOUNT_LOCKED(2006, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(2007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(2009, "账号下线"),
    USER_ACCOUNT_REGISTER_ERROR(2010, "账号注册错误"),

    USER_TYPE_EXCEPTION(2101, "用户类别异常"),

    USER_NO_PERMISSION(2403, "用户无权限"),
    USER_CAPTCHA_CODE_ERROR(2500, "验证码错误"),

     /* 邮箱错误 3001-4000 */
    EMAIL_PATTERN_ERROR(3001, "邮箱格式错误"),
    EMAIL_SEND_FAIL(3002, "邮箱发送失败"),

    EMAIL_NOT_EXIST_RECORD(3101, "邮箱不存在记录"),
    EMAIL_CAPTCHA_CODE_COUNT_EXHAUST(3103, "申请次数达到上限"),

    /* 组卷错误 4001-5000 */
    QUESTION_LIBRARY_NOT_EXISTS(4001, "题库不存在"),
    QUESTION_PAPER_LIBRARY_NOT_EXISTS(4002, "试卷库不存在"),
    QUESTION_NOT_EXISTS(4003, "题目不存在"),
    QUESTION_PAPER_NOT_EXISTS(4004, "试卷不存在"),
    QUESTION_NOT_EXISTS_IN_PAPER(4005, "题目没有出现在试卷中"),

    /* 招新错误 5001-6000 */
    RECRUITMENT_BATCH_NOT_EXISTS(5001, "招新批次不存在"),
    RECRUITMENT_BATCH_IS_RUN(5002, "招新已开始"),
    RECRUITMENT_BATCH_IS_NOT_RUN(5003, "招新未开始"),

    RECRUITMENT_ACTIVITY_NOT_EXISTS(5101, "招新活动不存在"),
    RECRUITMENT_ACTIVITY_IS_RUN(5102, "招新活动已开始"),
    RECRUITMENT_ACTIVITY_IS_NOT_RUN(5103, "招新活动未开始"),
    PERIOD_NOT_EXISTS(5104, "时间段不存在"),
    USER_CANNOT_PARTICIPATE_IN_ACTIVITY(5105, "用户不能参与此活动"),
    ACTIVITY_PARTICIPATION_NOT_EXISTS(5106, "用户活动参与票据不存在"),

    USER_RESUME_NOT_EXISTS(5201, "用户简历不存在"),
    USER_DID_NOT_PARTICIPATE(5202, "用户并没有参与活动"),
    USER_RESUME_SUBMIT_OVER_COUNT(5203, "用户简历提交次数已达最大"),
    USER_RESUME_STATUS_EXCEPTION(5204, "用户简历状态异常"),
    USER_RESUME_STATUS_TRANS_EVENT_ERROR(5205, "用户简历状态流转失败"),
    USER_RESUME_CONFIRMED(5206, "用户简历已转正"),

    /* 面试错误 6001-7000 */
    INTERVIEW_SCHEDULE_NOT_EXISTS(6001, "面试预约不存在"),
    INTERVIEWER_NOT_EXISTS(6002, "面试官不存在"),
    INTERVIEW_SCHEDULE_IS_REFERENCED(6003, "面试预约已被引用"),

    INTERVIEW_NOT_EXISTS(6101, "本场面试不存在"),
    INTERVIEW_STATUS_EXCEPTION(6102, "面试状态异常"),
    INTERVIEW_STATUS_TRANS_EVENT_ERROR(6102, "面试状态流转失败"),

    INTERVIEW_COMMENT_NOT_EXISTS(6201, "面试评论不存在"),
    INTERVIEW_NOT_SUMMARIZED(6202, "面试还未总结"),

    /* 资源错误 7001-8000 */
    RESOURCE_LEVEL_NOT_EXISTS(7001, "资源权限不存在"),
    RESOURCE_NOT_EXISTS(7002, "资源不存在"),
    RESOURCE_CANNOT_BE_ACCESSED(7003, "资源不能被访问"),
    RESOURCE_UPLOAD_FAILED(7004, "资源上传失败"),
    RESOURCE_UPLOAD_TOO_FREQUENT(7005, "资源上传太频繁了"),
    RESOURCE_UPLOAD_BLOCKED(7006, "资源上传被阻止"),
    RESOURCE_REMOVE_FAILED(7007, "资源删除失败"),
    RESOURCE_LOAD_FAILED(7008, "资源加载失败"),
    RESOURCE_PREVIEW_FAILED(7009, "资源预览失败"),
    RESOURCE_DOWNLOAD_FAILED(7010, "资源下载失败"),
    RESOURCE_GET_OBJECT_URL_FAILED(7011, "获取资源 url 失败"),
    RESOURCE_NOT_VALID(7012, "资源非法"),
    RESOURCE_IS_BLANK(7013, "资源为空"),
    RESOURCE_OUT_SIZE(7014, "资源过大"),
    RESOURCE_TYPE_NOT_MATCH(7015, "资源类型不匹配"),


    /* -------------- */;

    private final Integer code;
    private final String message;

    /**
     * 根据code获取message
     *
     * @param code 状态码的code
     * @return 状态码的状态信息
     */
    public static String GetStatusMsgByCode(Integer code) {
        for (GlobalServiceStatusCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }
}
