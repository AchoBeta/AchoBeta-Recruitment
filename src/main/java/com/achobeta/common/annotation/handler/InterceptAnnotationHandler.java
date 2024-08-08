package com.achobeta.common.annotation.handler;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.common.annotation.Intercept;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class InterceptAnnotationHandler {

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
        if(targetMethod.isAnnotationPresent(Intercept.class)) {
            intercept = targetMethod.getAnnotation(Intercept.class);
        }
        return intercept;
    }

    public static Boolean isIgnore(Intercept intercept) {
        // 没有 Intercept 注解或者注解 ignore 为 true 则无需拦截
        return Objects.isNull(intercept) || intercept.ignore();
    }

    public static Boolean isIgnore(Method targetMethod) {
        return isIgnore(getIntercept(targetMethod));
    }

    public static void validate(Intercept intercept, Integer role) {
        UserTypeEnum[] permit = intercept.permit();
        log.info("api intercept permit: {}, current user role: {}", Arrays.toString(permit), role);
        // permit 中没有 role 就会抛异常
        Arrays.stream(permit)
                .map(UserTypeEnum::getCode)
                .distinct()
                .filter(type -> type.equals(role))
                .findAny()
                .orElseThrow(() -> new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION));
    }

    public static void validate(Method targetMethod, Integer role) {
        validate(getIntercept(targetMethod), role);
    }

}
