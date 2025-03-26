package com.onseju.matchingservice.events.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.onseju.matchingservice.config.RabbitMQConfig;
import com.onseju.matchingservice.events.MatchedEvent;
import com.onseju.matchingservice.events.OrderBookSyncedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 이벤트 발행 서비스
 * 도메인 이벤트를 RabbitMQ를 통해 발행하는 기능 제공
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingEventPublisher {

	private final RabbitTemplate rabbitTemplate;

	/**
	 * 주문 매칭 이벤트 발행
	 */
	public void publishOrderMatched(final MatchedEvent event) {
		rabbitTemplate.convertAndSend(
				RabbitMQConfig.ONSEJU_EXCHANGE, RabbitMQConfig.ORDER_MATCHED_KEY, event);
	}

	/**
	 * 호가창 이벤트 발행
	 */
	public void publishOrderBookSynced(final OrderBookSyncedEvent event) {
		log.info("호가창 이벤트 발행");
		rabbitTemplate.convertAndSend(
				RabbitMQConfig.ONSEJU_EXCHANGE, RabbitMQConfig.ORDER_BOOK_SYNCED_KEY, event);
	}
}
