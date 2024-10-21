package com.achobeta.common.annotation.handler;

import com.achobeta.common.annotation.Accessible;
import com.achobeta.util.HttpRequestUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class AccessibleValidator implements ConstraintValidator<Accessible, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // 可以为 null 但是必须可以访问
        return Optional.ofNullable(s)
                .map(url -> {
                    try {
                        return HttpRequestUtil.isAccessible(url);
                    } catch (Exception e) {
                        log.warn(e.getMessage());
                        return Boolean.FALSE;
                    }
                }).orElse(Boolean.TRUE);
    }
}
