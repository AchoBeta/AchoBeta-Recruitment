package com.achobeta.domain.email.component.annotations;

import com.achobeta.domain.email.component.annotations.validator.EmailPatternValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailPatternValidator.class)
public @interface EmailPattern {

    String EMAIL_PATTERN = "^[\\w\\.-]+@[a-zA-Z\\d\\.-]+\\.[a-zA-Z]{2,}$";

    String message() default "邮箱格式非法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String pattern() default EMAIL_PATTERN;
}