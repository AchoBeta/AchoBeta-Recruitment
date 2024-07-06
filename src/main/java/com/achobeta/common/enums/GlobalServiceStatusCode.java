package com.achobeta.common.enums;

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

    USER_NO_PERMISSION(2403, "用户无权限"),
    USER_CAPTCHA_CODE_ERROR(2500, "验证码错误"),

     /* 邮箱错误 3001-4000 */
    EMAIL_PATTERN_ERROR(3001, "邮箱格式错误"),
    EMAIL_SEND_FAIL(3002, "邮箱发送失败"),

    EMAIL_NOT_EXIST_RECORD(3101, "邮箱不存在记录"),
    EMAIL_CAPTCHA_CODE_COUNT_EXHAUST(3103, "申请次数达到上限"),

    /* 招新错误 4001-5000 */
    RECRUITMENT_NOT_EXISTS(4001, "本场招新不存在"),
    RECRUITMENT_IS_RUN(4002, "招新已开始"),
    RECRUITMENT_IS_NOT_RUN(4003, "招新未开始"),
    RECRUITMENT_NOT_SET_PAPER(4004, "招新未设置题单"),

    // todo: 同步更新一下这些枚举
    QUESTIONNAIRE_NOT_EXISTS(4101, "问卷不存在"),
    ENTRY_NOT_EXISTS(4102, "问题不存在"),
    ENTRY_EXISTS_IN_PAPER(4103, "问题已出现在试卷中"),

    PERIOD_NOT_EXISTS(4201, "时间段不存在"),
    PERIOD_IS_SELECTED(4202,"时间段被选中了"),
    PERIOD_IS_NOT_SELECTED(4203, "时间段未选中"),

    QUESTION_LIBRARY_NOT_EXISTS(4301, "题库不存在"),
    QUESTION_PAPER_LIBRARY_NOT_EXISTS(4302, "题单库不存在"),
    QUESTION_NOT_EXISTS(4303, "题目不存在"),
    QUESTION_PAPER_NOT_EXISTS(4304, "题单不存在"),


    /* -------------- */;

    private final Integer code;
    private final String message;

    GlobalServiceStatusCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

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
