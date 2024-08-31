package com.achobeta.common.annotation.handler;

import com.achobeta.common.annotation.IntRange;
import com.achobeta.common.annotation.Intercept;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-07
 * Time: 17:34
 */
public class IntRangeValidator implements ConstraintValidator<IntRange, Object> {

    private int min;

    private int max;

    @Override
    public void initialize(IntRange intRange) {
        this.min = intRange.min();
        this.max = intRange.max();
    }

    private int compare(Number number1, Number number2) {
        return Double.compare(number1.doubleValue(), number2.doubleValue());
    }

    private boolean isValid(Object value) {
        if (value instanceof Number number) {
            return compare(number, min) >= 0 && compare(number, max) <= 0;
        } else if (value instanceof Collection<?> collection) {
            return collection.stream()
                    .filter(v -> !isValid(v))
                    .toList()
                    .isEmpty();
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return isValid(value);
    }
}
