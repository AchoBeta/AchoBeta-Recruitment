package com.achobeta.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.achobeta.domain.message.service.MessageService.*;

/**
 * @author cattleYuan
 * @date 2024/8/27
 */
@Configuration
public class RabbitMqConfig {
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue messageSendDeadQueue(){
        return new Queue(MESSAGE_SEND_DEAD_QUEUE);
    }
    @Bean
    public DirectExchange messageSendDeadExchange(){
        return new DirectExchange(MESSAGE_SEND_DEAD_EXCHANGE);
    }
    @Bean
    Binding bindingQueueToExchangeWithDead(){
        return BindingBuilder.bind(messageSendDeadQueue()).to(messageSendDeadExchange()).with(MESSAGE_SEND_DEAD_KEY);
    }

    @Bean
    public Queue messageSendQueue(){
        return QueueBuilder.durable(MESSAGE_SEND_QUEUE)
                .deadLetterExchange(MESSAGE_SEND_DEAD_EXCHANGE)
                .deadLetterRoutingKey(MESSAGE_SEND_DEAD_KEY).build();
    }

    @Bean
    public DirectExchange messageSendExchange(){
        return new DirectExchange(MESSAGE_SEND_EXCHANGE);
    }
    @Bean
    public Binding bindingQueueToMessageSendExchange(){
        return BindingBuilder.bind(messageSendQueue()).to(messageSendExchange()).with(MESSAGE_SEND_KEY);
    }

}
