package com.achobeta.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {

    MALE(0, "男"),
    FEMALE(1, "女"),
    NON_BINARY(2, "非二元性别"),
    TRANSGENDER(3, "跨性别"),
    GENDER_FLUID(4, "性别流动"),
    AGENDER(5, "无性别"),
    BIGENDER(6, "双性别");

    @EnumValue
    @JsonValue
    private final Integer gender;

    private final String description;

    Gender(Integer gender, String description) {
        this.gender = gender;
        this.description = description;
    }

    public Integer getGender() {
        return gender;
    }

    public String getDescription() {
        return description;
    }
}
