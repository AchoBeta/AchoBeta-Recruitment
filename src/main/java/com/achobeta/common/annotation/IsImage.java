package com.achobeta.common.annotation;

import com.achobeta.common.annotation.handler.IsImageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Created With Intellij IDEA
 * User: 马拉圈
 * Date: 2024-08-23
 * Time: 2:04
 * Description: 此注解用于检测资源是否可访问，并且是图片类型的资源
 */
@Documented
@Constraint(validatedBy = {IsImageValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsImage {

    String message() default "image url 非法"; // 默认消息

    Class<?>[] groups() default {}; // 分组校验

    Class<? extends Payload>[] payload() default {}; // 负载信息

}
