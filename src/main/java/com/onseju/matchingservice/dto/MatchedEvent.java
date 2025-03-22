package com.onseju.matchingservice.dto;

import java.math.BigDecimal;

public record MatchedEvent(
        String companyCode,
        Long buyOrderId,
        Long buyAccountId,
        Long sellOrderId,
        Long sellAccountId,
        BigDecimal quantity,
        BigDecimal price,
        Long tradeAt
) {
}
