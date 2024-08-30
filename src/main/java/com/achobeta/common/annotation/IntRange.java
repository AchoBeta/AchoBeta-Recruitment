package com.achobeta.common.annotation;

import com.achobeta.common.annotation.handler.IntRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 17:19
 */
@Documented
@Constraint(validatedBy = {IntRangeValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "数值或集合不能为 null")
public @interface IntRange {

    String message() default "数值不在有效范围内"; // 默认消息

    int min();

    int max();

    Class<?>[] groups() default {}; // 分组校验

    Class<? extends Payload>[] payload() default {}; // 负载信息
}
