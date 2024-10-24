package com.achobeta.common.annotation;

import com.achobeta.common.annotation.handler.MobilePhoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-17
 * Time: 21:59
 */
@Documented
@Constraint(validatedBy = {MobilePhoneValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MobilePhone {

    String message() default "手机号非法"; // 默认消息

    Class<?>[] groups() default {}; // 分组校验

    Class<? extends Payload>[] payload() default {}; // 负载信息
}
