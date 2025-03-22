package com.onseju.matchingservice.dto;

import com.onseju.matchingservice.domain.OrderStatus;
import com.onseju.matchingservice.domain.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderedEvent(
        Long id,
        String companyCode,
        Type type,
        OrderStatus status,
        BigDecimal totalQuantity,
        BigDecimal remainingQuantity,
        BigDecimal price,
        LocalDateTime createdDateTime,
        Long accountId
) {
}
