package com.achobeta.email.provider.strategy;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-13
 * Time: 22:28
 */
@Component
public class RoundRobinProvideStrategy implements ProvideStrategy {

    private final static AtomicInteger COUNTER = new AtomicInteger(0);

    @Override
    public JavaMailSenderImpl getSender(List<JavaMailSenderImpl> senderList) {
        return senderList.get(COUNTER.getAndIncrement() % senderList.size());
    }

}
