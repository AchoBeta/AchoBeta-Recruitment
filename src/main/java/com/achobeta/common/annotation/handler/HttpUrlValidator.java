package com.achobeta.common.annotation.handler;

import com.achobeta.common.annotation.HttpUrl;
import com.achobeta.util.HttpRequestUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-19
 * Time: 11:35
 */
public class HttpUrlValidator implements ConstraintValidator<HttpUrl, String> {

    @Override
    public boolean isValid(String url, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(url)
                .map(s -> HttpRequestUtil.HTTP_URL_PATTERN.matcher(s).matches())
                .orElse(Boolean.TRUE);
    }
}
