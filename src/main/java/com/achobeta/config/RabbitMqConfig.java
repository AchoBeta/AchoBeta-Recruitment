package com.achobeta.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cattleYuan
 * @date 2024/8/22
 */
@Configuration
public class RabbitMqConfig {
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public Queue messageDeadQueue(){
        return new Queue("message.dead.queue");
    }
    @Bean
    public DirectExchange messageDeadExchange(){
        return new DirectExchange("message.dead.exchange");
    }
    @Bean
    Binding bindingQueueToExchangewithDead(){
        return BindingBuilder.bind(messageDeadQueue()).to(messageDeadExchange()).with("ttl");
    }
    @Bean
    public Queue MessageQueue(){
        return QueueBuilder.durable("message.queue")
                .deadLetterExchange("message.dead.exchange")
                .deadLetterRoutingKey("ttl").build();
    }

    @Bean
    public DirectExchange MessageExchange(){
        return new DirectExchange("message.exchange");
    }
    @Bean
    public Binding bindingQueueToActivityExchange(){
        return BindingBuilder.bind(MessageQueue()).to(MessageExchange()).with("mess");
    }

}
