package com.achobeta.common.constants;

import lombok.Getter;

@Getter
public enum RoleKinds {
    USER("user",0),
    ADMINER("adminer",1)
    ;
    private final String roleName;
    private final Integer roleNumber;

    RoleKinds(String roleName, Integer roleNum) {
        this.roleName = roleName;
        this.roleNumber = roleNum;
    }

}
