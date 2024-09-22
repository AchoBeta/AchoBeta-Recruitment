package com.achobeta.common.annotation;

import com.achobeta.common.annotation.handler.IsAccessibleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-20
 * Time: 23:50
 */
@Documented
@Constraint(validatedBy = {IsAccessibleValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsAccessible {

    String message() default "url 非法"; // 默认消息

    Class<?>[] groups() default {}; // 分组校验

    Class<? extends Payload>[] payload() default {}; // 负载信息

}
