package com.achobeta.domain.users.model.vo;

import lombok.Builder;
import lombok.Getter;

/**
 * 生命周期只有一次，成员变量对修改关闭，直接通过 builder 构建对象
 */
@Getter
@Builder
public class VerificationCodeTemplate {

    private String code; // 验证码

    private int minutes; // 过期时间分钟数

}
