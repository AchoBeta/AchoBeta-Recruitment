package com.achobeta.common.annotation.handler;

import com.achobeta.common.annotation.IntRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.function.Consumer;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 17:34
 */
public class IntRangeValidator implements ConstraintValidator<IntRange, Integer> {

    private int min;

    private int max;

    @Override
    public void initialize(IntRange intRange) {
        this.min = intRange.min();
        this.max = intRange.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value >= min && value <= max;
    }
}
