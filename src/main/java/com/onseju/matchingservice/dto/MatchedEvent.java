package com.onseju.matchingservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record MatchedEvent(
		UUID id,
        String companyCode,
        Long buyOrderId,
        Long sellOrderId,
        BigDecimal quantity,
        BigDecimal price,
        Long tradeAt
) {
}
