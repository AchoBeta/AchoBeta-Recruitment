package com.achobeta.common.annotation.handler;

import com.achobeta.common.annotation.IsAccessible;
import com.achobeta.util.MediaUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class IsAccessibleValidator implements ConstraintValidator<IsAccessible, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // 可以为 null 但是必须可以访问
        return Optional.ofNullable(s)
                .map(url -> {
                    try {
                        return MediaUtil.isAccessible(url);
                    } catch (IOException e) {
                        log.warn(e.getMessage());
                        return Boolean.FALSE;
                    }
                }).orElse(Boolean.TRUE);
    }
}
