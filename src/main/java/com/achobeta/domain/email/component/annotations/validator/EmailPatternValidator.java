package com.achobeta.domain.email.component.annotations.validator;

import com.achobeta.domain.email.component.annotations.EmailPattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Pattern;


public class EmailPatternValidator implements ConstraintValidator<EmailPattern, String> {

    // email的格式
    private String pattern;


    @Override
    public void initialize(EmailPattern constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern emailPattern = Pattern.compile(pattern);
        return Objects.nonNull(value) && emailPattern.matcher(value).matches();
    }
}
