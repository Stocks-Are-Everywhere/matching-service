package com.onseju.matchingservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 메시지 브로커 설정
 * 서비스 간 비동기 통신을 위한 Exchange, Queue, Binding 정의
 */
@Configuration
public class RabbitMQConfig {

	// Exchange 정의
	public static final String ONSEJU_EXCHANGE = "onseju.exchange";

	// Queue 정의 - 주문 서비스
	public static final String ORDER_CREATED_QUEUE = "order.created.queue";
	public static final String ORDER_BOOK_SYNCED_QUEUE = "order-book.synced.queue";

	// Routing Key 정의
	public static final String ORDER_CREATED_KEY = "order.created";
	public static final String ORDER_MATCHED_KEY = "order.matched";
	public static final String ORDER_BOOK_SYNCED_KEY = "order-book.synced";

	// Exchange 생성
	@Bean
	public TopicExchange onsejuExchange() {
		return new TopicExchange(ONSEJU_EXCHANGE);
	}

	// 주문 서비스 Queues
	@Bean
	public Queue orderCreatedQueue() {
		return new Queue(ORDER_CREATED_QUEUE, true);
	}

	@Bean
	public Queue orderBookSyncedQueue() {
		return new Queue(ORDER_BOOK_SYNCED_QUEUE, true);
	}

	// Bindings - 주문 서비스
	@Bean
	public Binding orderBookSyncedBinding() {
		return BindingBuilder
				.bind(orderBookSyncedQueue())
				.to(onsejuExchange())
				.with(ORDER_BOOK_SYNCED_KEY);
	}

	// JSON 메시지 변환 설정
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	// RabbitTemplate 설정
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(jsonMessageConverter());
		return template;
	}
}
