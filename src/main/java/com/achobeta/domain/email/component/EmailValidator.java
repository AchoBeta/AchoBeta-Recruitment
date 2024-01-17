package com.achobeta.domain.email.component;

public class EmailValidator {

    public static final String EMAIL_PATTERN = "^[\\w\\.-]+@[a-zA-Z\\d\\.-]+\\.[a-zA-Z]{2,}$";


    public static boolean isEmailAccessible(String email) {
        return email.matches(EMAIL_PATTERN);
    }
}