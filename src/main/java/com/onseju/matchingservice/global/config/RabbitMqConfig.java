package com.onseju.matchingservice.global.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${spring.rabbitmq.port}")
    private String port;
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("matched.exchange");

    }

    @Bean
    public Queue orderServiceQueue() {
        return new Queue("matched.order.queue");
    }

    @Bean
    public Queue userServiceQueue() {
        return new Queue("matched.user.queue");
    }

    @Bean
    Binding bindingToOrderService(FanoutExchange fanoutExchange, Queue orderServiceQueue) {
        return BindingBuilder
                .bind(orderServiceQueue)
                .to(fanoutExchange);
    }

    @Bean
    Binding bindingToUserService(FanoutExchange fanoutExchange, Queue userServiceQueue) {
        return BindingBuilder
                .bind(userServiceQueue)
                .to(fanoutExchange);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setPort(Integer.parseInt(port));
        connectionFactory.setHost(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory());
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}