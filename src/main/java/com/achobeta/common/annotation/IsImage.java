package com.achobeta.common.annotation;

import com.achobeta.common.annotation.handler.IntRangeValidator;
import com.achobeta.common.annotation.handler.IsImageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-23
 * Time: 2:04
 */
@Documented
@Constraint(validatedBy = {IsImageValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "image url 不能为空")
public @interface IsImage {

    String message() default "image url 非法"; // 默认消息

    Class<?>[] groups() default {}; // 分组校验

    Class<? extends Payload>[] payload() default {}; // 负载信息

}
