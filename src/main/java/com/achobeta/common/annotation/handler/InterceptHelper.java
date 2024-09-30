package com.achobeta.common.annotation.handler;

import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-08
 * Time: 17:32
 */
public class InterceptHelper {

    public static Intercept getIntercept(Class<?> clazz) {
        // 类上的 Intercept 为初步结果
        return clazz.isAnnotationPresent(Intercept.class) ? clazz.getAnnotation(Intercept.class) : null;
    }

    public static Intercept getIntercept(Method targetMethod) {
        // 获取目标方法所在的类
        Class<?> declaringClass = targetMethod.getDeclaringClass();
        // 类上的 Intercept 为初步结果
        Intercept intercept = getIntercept(declaringClass);
        // 方法上的 Intercept 为最终结果
        return targetMethod.isAnnotationPresent(Intercept.class) ? targetMethod.getAnnotation(Intercept.class) : intercept;
    }

    public static boolean isIgnore(Intercept intercept) {
        // 没有 Intercept 注解或者注解 ignore 为 true 则无需拦截
        return Objects.isNull(intercept) || intercept.ignore();
    }

    public static boolean isIgnore(Method targetMethod) {
        return isIgnore(getIntercept(targetMethod));
    }

    public static boolean shouldPrintLog(Intercept intercept) {
        return Objects.isNull(intercept) || intercept.log();
    }

    public static boolean shouldPrintLog(Method targetMethod) {
        return shouldPrintLog(getIntercept(targetMethod));
    }

    public static boolean isValid(Intercept intercept, UserTypeEnum role) {
        // permit 中没有 role 就会抛异常
        return Arrays.stream(intercept.permit())
                .distinct()
                .filter(Objects::nonNull)
                .anyMatch(type -> type.equals(role));
    }

    public static boolean isValid(Method targetMethod, UserTypeEnum role) {
        return isValid(getIntercept(targetMethod), role);
    }

}
