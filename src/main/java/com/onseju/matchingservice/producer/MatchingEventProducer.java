package com.onseju.matchingservice.producer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.onseju.matchingservice.config.RabbitMQConfig;
import com.onseju.matchingservice.dto.MatchedEvent;
import com.onseju.matchingservice.producer.exception.MatchingEventProduceFailException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchingEventProducer {
    private static final int MESSAGE_TIMEOUT_SECONDS = 5;
    private static final int OPERATION_TIMEOUT_SECONDS = 10;

    private final RabbitTemplate rabbitTemplate;

    public CompletableFuture<Void> matchedOrder(MatchedEvent matchedEvent) {
        return CompletableFuture.runAsync(() -> {
            validateMatchedEvent(matchedEvent);
            publishMatchedEvent(matchedEvent);
        }).orTimeout(OPERATION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    private void validateMatchedEvent(MatchedEvent event) {
        if (event == null || event.sellOrderId() == null || event.buyOrderId() == null) {
            throw new IllegalArgumentException("Invalid matched event");
        }
    }

    private void publishMatchedEvent(MatchedEvent matchedEvent) {
        String correlationId = generateCorrelationId(matchedEvent);
        try {
            sendMessage(
                RabbitMQConfig.MATCHING_EXCHANGE,
                RabbitMQConfig.MATCHING_RESULT_KEY,
                matchedEvent,
                correlationId
            );
            log.info("체결 이벤트 발행 완료: {}", matchedEvent);
        } catch (Exception ex) {
            log.error("체결 이벤트 발행 중 오류 발생. correlationId: {}", correlationId, ex);
            throw new MatchingEventProduceFailException();
        }
    }

    private String generateCorrelationId(MatchedEvent event) {
        return String.format("trade-%s-%s", event.sellOrderId(), event.buyOrderId());
    }

    private void sendMessage(String exchange, String routingKey,
            MatchedEvent event, String correlationId) {
        try {
            CorrelationData correlation = new CorrelationData(correlationId);
            rabbitTemplate.convertAndSend(exchange, routingKey, event, correlation);

            CorrelationData.Confirm confirm = correlation.getFuture()
                .get(MESSAGE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            if (confirm == null || !confirm.isAck()) {
                throw new MatchingEventProduceFailException();
            }
        } catch (Exception e) {
            throw new MatchingEventProduceFailException();
        }
    }
}