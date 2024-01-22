package com.achobeta.common.enums;

import lombok.Getter;

@Getter
public enum UserType {

    USER("user",1),
    ADMIN("admin",2)
    ;

    private final String name;
    private final Integer code;

    UserType(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

}
