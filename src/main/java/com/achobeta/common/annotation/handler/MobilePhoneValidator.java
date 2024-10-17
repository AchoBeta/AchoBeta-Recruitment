package com.achobeta.common.annotation.handler;

import com.achobeta.common.annotation.MobilePhone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-17
 * Time: 22:00
 */
public class MobilePhoneValidator implements ConstraintValidator<MobilePhone, String> {

    private final static Pattern MOBILE_PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(phone)
                .map(s -> MOBILE_PHONE_PATTERN.matcher(s).matches())
                .orElse(Boolean.TRUE);
    }

}
