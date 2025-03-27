package com.onseju.matchingservice.events.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.onseju.matchingservice.config.RabbitMQConfig;
import com.onseju.matchingservice.events.MatchedEvent;
import com.onseju.matchingservice.events.OrderBookSyncedEvent;
import com.onseju.matchingservice.events.exception.MatchingEventPublisherFailException;
import com.onseju.matchingservice.events.exception.OrderBookSyncEventPublisherFailException;

import lombok.extern.slf4j.Slf4j;

/**
 * 이벤트 발행 서비스
 * 도메인 이벤트를 RabbitMQ를 통해 발행하는 기능 제공
 */
@Component
@Slf4j
public class OrderBookSyncEventPublisher extends AbstractEventPublisher<OrderBookSyncedEvent> {
    public OrderBookSyncEventPublisher(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

	@Override
    protected void validateEvent(OrderBookSyncedEvent event) {
		//TODO: EVENT ID 추가 후 수정
        // if (event == null || event.id() == null) {
        if (event == null) {
            throw new IllegalArgumentException("Invalid order event");
        }
    }

	@Override
    protected void doPublish(OrderBookSyncedEvent event) {
        try {
            publishOrderBookSyncEventToOrderSevice(event);
            // log.info("체결 이벤트 발행 완료. orderId: {}", event.id());
        } catch (Exception ex) {
            // log.error("체결 이벤트 발행 중 오류 발생. orderId: {}", event.id(), ex);
            throw new OrderBookSyncEventPublisherFailException();
        }
    }

	private void publishOrderBookSyncEventToOrderSevice(OrderBookSyncedEvent event){
		sendMessage(
			RabbitMQConfig.ONSEJU_MATCHING_EXCHANGE,
			RabbitMQConfig.ORDER_BOOK_SYNCED_KEY,
			event,
			""
		);
	}
}
