package com.achobeta.common.enums;

import lombok.Getter;

@Getter
public enum UserTypeEnum {

    USER("user",1),
    ADMIN("admin",2)
    ;

    private final String name;
    private final Integer code;

    UserTypeEnum(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

}
