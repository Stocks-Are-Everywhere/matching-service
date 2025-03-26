package com.onseju.matchingservice.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.onseju.matchingservice.config.RabbitMQConfig;
import com.onseju.matchingservice.domain.TradeOrder;
import com.onseju.matchingservice.dto.OrderedEvent;
import com.onseju.matchingservice.engine.MatchingEngine;
import com.onseju.matchingservice.mapper.EventMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchingEventListener {

    private final EventMapper eventMapper;
    private final MatchingEngine matchingEngine;

    /**
     * 주문 생성 이벤트를 받아 체결 엔진으로 넘긴다.
     */
    @RabbitListener(queues = RabbitMQConfig.MATCHING_REQUEST_QUEUE)
    public void handleMatchingRequestEvent(OrderedEvent orderedEvent) {
        log.info("Received Matching Request Event {}", orderedEvent);
        TradeOrder order = eventMapper.toTradeOrder(orderedEvent);
        matchingEngine.processOrder(order);
    }
}
