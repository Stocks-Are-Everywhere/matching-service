package com.onseju.matchingservice.handler;

import com.onseju.matchingservice.domain.TradeOrder;
import com.onseju.matchingservice.dto.OrderedEvent;
import com.onseju.matchingservice.engine.MatchingEngine;
import com.onseju.matchingservice.mapper.EventMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventHandler {

    private final MatchingEngine matchingEngine;
    private final EventMapper eventMapper;

    /**
     * 주문 생성 이벤트를 받아 체결 엔진으로 넘긴다.
     */
    @Async
    @RabbitListener(queues = "matching.event.queue")
    public void handleOrderEvent(OrderedEvent orderedEvent) {
        TradeOrder order = eventMapper.toTradeOrder(orderedEvent);
        matchingEngine.processOrder(order);
    }
}
