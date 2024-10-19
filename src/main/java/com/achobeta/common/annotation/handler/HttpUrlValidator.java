package com.achobeta.common.annotation.handler;

import com.achobeta.common.annotation.HttpUrl;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-19
 * Time: 11:35
 */
public class HttpUrlValidator implements ConstraintValidator<HttpUrl, String> {

    private static final Pattern HTTP_URL_PATTERN = Pattern.compile("^(?i)(http|https):(//(([^@\\[/?#]*)@)?(\\[[\\p{XDigit}:.]*[%\\p{Alnum}]*]|[^\\[/?#:]*)(:(\\{[^}]+\\}?|[^/?#]*))?)?([^?#]*)(\\?([^#]*))?(#(.*))?");

    @Override
    public boolean isValid(String url, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(url)
                .map(s -> HTTP_URL_PATTERN.matcher(s).matches())
                .orElse(Boolean.TRUE);
    }
}
