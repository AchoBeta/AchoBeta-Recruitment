package com.achobeta.domain.users.service;

import org.springframework.stereotype.Service;


public interface EmailService {

    void sendIdentifyingCode(String email, String code);

    void checkIdentifyingCode(String redisKey, String code);
}
