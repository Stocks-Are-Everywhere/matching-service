package com.onseju.matchingservice.events.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.onseju.matchingservice.config.RabbitMQConfig;
import com.onseju.matchingservice.events.MatchedEvent;
import com.onseju.matchingservice.events.exception.MatchingEventPublisherFailException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 이벤트 발행 서비스
 * 도메인 이벤트를 RabbitMQ를 통해 발행하는 기능 제공
 */
@Component
@Slf4j
public class MatchingEventPublisher extends AbstractEventPublisher<MatchedEvent> {
    public MatchingEventPublisher(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

	@Override
    protected void validateEvent(MatchedEvent event) {
		//TODO: EVENT ID 추가 후 수정
        // if (event == null || event.id() == null) {
        if (event == null) {
            throw new IllegalArgumentException("Invalid order event");
        }
    }

	@Override
    protected void doPublish(MatchedEvent event) {
        try {
            publishAfterMatchingEventToOrderSevice(event);
            // log.info("체결 이벤트 발행 완료. orderId: {}", event.id());
        } catch (Exception ex) {
            // log.error("체결 이벤트 발행 중 오류 발생. orderId: {}", event.id(), ex);
            throw new MatchingEventPublisherFailException();
        }
    }

	private void publishAfterMatchingEventToOrderSevice(MatchedEvent event){
		sendMessage(
			RabbitMQConfig.ONSEJU_MATCHING_EXCHANGE,
			RabbitMQConfig.MATCHING_RESULT_KEY,
			event,
			""
		);
	}

	//
	// /**
	//  * 주문 매칭 이벤트 발행
	//  */
	// public void publishOrderMatched(final MatchedEvent event) {
	// 	rabbitTemplate.convertAndSend(
	// 			RabbitMQConfig.ONSEJU_EXCHANGE, RabbitMQConfig.ORDER_MATCHED_KEY, event);
	// }
	//
	// /**
	//  * 호가창 이벤트 발행
	//  */
	// public void publishOrderBookSynced(final OrderBookSyncedEvent event) {
	// 	log.info("호가창 이벤트 발행");
	// 	rabbitTemplate.convertAndSend(
	// 			RabbitMQConfig.ONSEJU_EXCHANGE, RabbitMQConfig.ORDER_BOOK_SYNCED_KEY, event);
	// }
}
