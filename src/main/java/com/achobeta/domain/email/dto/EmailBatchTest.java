package com.achobeta.domain.email.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailBatchTest {

    @Test
    void test() {
        EmailBatch emailBatch = new EmailBatch();
        // 邮箱
        emailBatch.setSender("1111");
//        emailBatch.setContent("测试内容");
        System.out.println(ValidatorUtils.validateEntity(emailBatch));
    }
}