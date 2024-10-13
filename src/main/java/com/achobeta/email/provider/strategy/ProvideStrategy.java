package com.achobeta.email.provider.strategy;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-13
 * Time: 22:22
 */
@Validated
public interface ProvideStrategy {

    String BASE_NAME = "ProvideStrategy";

    JavaMailSenderImpl getSender(@NotEmpty List<JavaMailSenderImpl> senderList);
}
