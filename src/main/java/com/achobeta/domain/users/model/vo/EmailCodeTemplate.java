package com.achobeta.domain.users.model.vo;

import lombok.Data;


@Data
public class EmailCodeTemplate {

    private String code; // 验证码

    private int minutes; // 过期时间分钟数

}
