package com.achobeta.domain.message.service.strategy;

import com.achobeta.domain.message.model.dto.MessageSendDTO;
import org.springframework.stereotype.Component;


import static com.achobeta.domain.message.service.strategy.MessageSendStrategy.SEND_BASE_NAME;

/**
 * @author cattleYuan
 * @Description: 类
 * @date 2024/8/21
 */
@Component("email"+SEND_BASE_NAME)
public class MSendWithEmailStrategy implements MessageSendStrategy{
    @Override
    public void sendMessage(MessageSendDTO messageSendBody) {

    }
}
