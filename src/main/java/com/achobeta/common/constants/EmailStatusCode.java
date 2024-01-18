package com.achobeta.common.constants;

import lombok.Getter;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-01-18
 * Time: 14:06
 */
@Getter
public enum EmailStatusCode {

    /* 邮箱错误 3001-4000 */
    EMAIL_NOT_EXIST(3101, "邮箱不存在"),
    EMAIL_NOT_OBTAIN_CODE(3102, "邮箱未获取验证码"),
    EMAIL_CODE_NOT_CONSISTENT(3103, "邮箱验证码不一致"),
    EMAIL_CODE_EXPIRE(3104, "邮箱验证码已过期"),
    EMAIL_CODE_OPPORTUNITIES_EXHAUST(3105, "验证次数达到上限"),


    /* -------------- */;

    private final Integer code;
    private final String message;

    EmailStatusCode(Integer code, String message) {
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
        for (EmailStatusCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }
}
