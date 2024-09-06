package com.achobeta.common.annotation;

import com.achobeta.common.annotation.handler.IntRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Created With Intellij IDEA
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 17:19
 * Description: 此注解用于判断数值是否在规定氛围内
 * min 代表最小值，max 代表最大值，被注解的变量数值必须在闭区间 [min, max]
 * 支持该变量是 Number 类型的变量，以及其数组、集合；
 * 对于数组和集合，必须每个元素都满足该规则，否则就不通过
 */
@Documented
@Constraint(validatedBy = {IntRangeValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntRange {

    String message() default "数值不在有效范围内"; // 默认消息

    int min();

    int max();

    Class<?>[] groups() default {}; // 分组校验

    Class<? extends Payload>[] payload() default {}; // 负载信息
}
