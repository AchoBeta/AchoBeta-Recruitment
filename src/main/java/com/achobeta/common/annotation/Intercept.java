package com.achobeta.common.annotation;

import com.achobeta.common.enums.UserTypeEnum;

import java.lang.annotation.*;

/**
 * Created With Intellij IDEA
 * User: 马拉圈
 * Date: 2024-08-08
 * Time: 12:50
 * Description: permit 代表允许访问接口方法的角色
 * 例如：
 * {UserTypeEnum.ADMIN, UserTypeEnum.USER} 代表两者均可访问
 * {UserTypeEnum.ADMIN} 代表只有管理员能访问
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Intercept {

    UserTypeEnum[] permit() default {};

    boolean ignore() default false;

}
