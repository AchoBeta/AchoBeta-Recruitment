package com.achobeta.common.enums;

import com.achobeta.exception.GlobalServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    USER("user",1),
    ADMIN("admin",2)
    ;

    private final String name;
    private final Integer code;

    public static UserTypeEnum get(Integer role) {
        for (UserTypeEnum userTypeEnum : UserTypeEnum.values()) {
            if(userTypeEnum.getCode().equals(role)) {
                return userTypeEnum;
            }
        }
        throw new GlobalServiceException(GlobalServiceStatusCode.USER_TYPE_EXCEPTION);
    }

}
