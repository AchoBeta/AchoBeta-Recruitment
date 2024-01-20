package com.achobeta.common.constants;

public enum RoleType {
    USER("user",0),
    ADMINER("adminer",1)
    ;
    private final String roleName;
    private final Integer roleNumber;

    public String getRoleName() {
        return roleName;
    }

    public Integer getRoleNumber() {
        return roleNumber;
    }

    RoleType(String roleName, Integer roleNum) {
        this.roleName = roleName;
        this.roleNumber = roleNum;
    }

}
