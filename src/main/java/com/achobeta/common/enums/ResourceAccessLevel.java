package com.achobeta.common.enums;

import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-22
 * Time: 22:58
 */
@Getter
@AllArgsConstructor
public enum ResourceAccessLevel {

    FREE_ACCESS(0, "没有限制", "free"),

    LOGIN_ACCESS(1, "登录即可访问", "login"),

    USER_ACCESS(2, "用户本人可访问", "user"),

    ADMIN_ACCESS(3, "仅管理员可访问", "admin"),

    NON_ACCESS(4, "不可访问", "non"),

    ;

    @EnumValue
    @JsonValue
    private final Integer level;

    private final String description;

    private final String beanPrefix;

    public static ResourceAccessLevel get(Integer level) {
        for (ResourceAccessLevel accessLevel : ResourceAccessLevel.values()) {
            if(accessLevel.getLevel().equals(level)) {
                return accessLevel;
            }
        }
        throw new GlobalServiceException(GlobalServiceStatusCode.RESOURCE_LEVEL_NOT_EXISTS);
    }

    public ResourceAccessLevel and(ResourceAccessLevel accessLevel) {
        return Optional.ofNullable(accessLevel).filter(otherLevel -> this.getLevel().compareTo(otherLevel.getLevel()) <= 0).orElse(this);
    }

}
