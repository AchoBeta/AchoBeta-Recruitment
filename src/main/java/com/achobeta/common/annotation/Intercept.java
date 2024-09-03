package com.achobeta.common.annotation;

import com.achobeta.common.enums.UserTypeEnum;

import java.lang.annotation.*;

/**
 * Created With Intellij IDEA
 * User: 马拉圈
 * Date: 2024-08-08
 * Time: 12:50
 * Description: 此注解用于辅助判断账户访问一个接口是否拦截
 * permit 代表允许访问接口方法的角色
 * {UserTypeEnum.ADMIN, UserTypeEnum.USER} 代表两者均可访问
 * {UserTypeEnum.ADMIN} 代表只有管理员能访问
 * {UserTypeEnum.USER} 代表只有用户能访问
 * {} 代表谁都没法访问
 * ignore == true 代表忽略拦截
 * ignore == false 默认进行拦截
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Intercept {

    UserTypeEnum[] permit() default {};

    boolean ignore() default false;

}
