package com.achobeta.domain.message.service.strategy;

import com.achobeta.domain.message.model.dto.MessageSendDTO;

public interface MessageSendStrategy {
    String SEND_BASE_NAME = "MessageSendStrategy";

    void sendMessage(MessageSendDTO messageSendBody);
}
