package com.achobeta.domain.feedback.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/9/5
 */
@Getter
@Setter
public class StuBaseInfo implements Serializable {
    /**
     * 学生姓名
     */
    private String name;

    /**
     * 学生年级
     */
    private Integer grade;

    /**
     * 学生性别
     */
    private Integer gender;
}
