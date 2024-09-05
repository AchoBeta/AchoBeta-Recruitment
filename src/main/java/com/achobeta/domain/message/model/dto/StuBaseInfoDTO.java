package com.achobeta.domain.message.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/9/3
 */
@Getter
@Setter
public class StuBaseInfoDTO implements Serializable {
    /*发送用户id*/
    private Long userId;
    /*用户姓名*/
    private String stuName;
}
