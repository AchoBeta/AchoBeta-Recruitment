package com.achobeta.domain.email.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@SpringBootTest
class EmailBatchTest {


    @Autowired
    private ValidatorUtils validatorUtils;

    @Test
    void test() {
        EmailBatch emailBatch = new EmailBatch();
        emailBatch.setSender("111");
        System.out.println(validatorUtils.validateEntity(emailBatch));
    }
}