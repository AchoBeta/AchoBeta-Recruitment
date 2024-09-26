package com.achobeta.domain.email.constants;

/**
 * @author BanTanger 半糖
 * @date 2024/1/22 16:52
 */
public interface EmailConstants {

    /**
     * 验证码集合
     */
    String CAPTCHA_CODES_KEY = "captcha_codes:";

    /**
     * 验证码
     */
    String CAPTCHA_CODE_KEY = "captcha_code";

    /**
     * 登录失败次数
     */
    String LOGIN_FAIL_CNT_KEY = "login_fail_cnt:";

    /**
     * 验证码生成次数
     */
    String CAPTCHA_CODE_CNT_KEY = "captcha_code_cnt:";

    /**
     * 风控用户列表
     */
    String RISK_CONTROLLER_USERS_KEY = "risk_controller_users:";

    /**
     * 防重提交
     */
    String REPEAT_SUBMIT_KEY = "repeat_submit:";

}
