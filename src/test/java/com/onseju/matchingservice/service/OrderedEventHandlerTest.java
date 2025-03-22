package com.onseju.matchingservice.service;

import com.onseju.matchingservice.domain.OrderStatus;
import com.onseju.matchingservice.domain.Type;
import com.onseju.matchingservice.dto.OrderedEvent;
import com.onseju.matchingservice.engine.MatchingEngine;
import com.onseju.matchingservice.handler.OrderEventHandler;
import com.onseju.matchingservice.mapper.EventMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class OrderedEventHandlerTest {

    @Autowired
    OrderEventHandler orderEventHandler;

    @Autowired
    EventMapper eventMapper;

    @Autowired
    MatchingEngine matchingEngine;

    @Test
    @DisplayName("이벤트를 전달받아 비동기로 처리한다.")
    void handleOrderEventShouldProcessOrder() {
        // given
        OrderedEvent orderedEvent = new OrderedEvent(
                1L,
                "005930",
                Type.LIMIT_BUY,
                OrderStatus.ACTIVE,
                new BigDecimal(100),
                new BigDecimal(100),
                new BigDecimal(100),
                LocalDateTime.now(),
                1L
        );

        // when
        CompletableFuture.runAsync(() -> orderEventHandler.handleOrderEvent(orderedEvent))
                .orTimeout(2, TimeUnit.SECONDS) // 비동기 실행을 기다림
                .join();

        // then
        Assertions.assertThatCode(() -> matchingEngine.processOrder(eventMapper.toTradeOrder(orderedEvent)))
                .doesNotThrowAnyException();
    }
}