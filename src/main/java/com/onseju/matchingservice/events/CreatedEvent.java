package com.onseju.matchingservice.events;

import java.math.BigDecimal;

import com.onseju.matchingservice.domain.OrderStatus;
import com.onseju.matchingservice.domain.Type;

import lombok.Builder;

@Builder
public record CreatedEvent(
		Long id,
		String companyCode,
		Type type,
		OrderStatus status,
		BigDecimal totalQuantity,
		BigDecimal remainingQuantity,
		BigDecimal price,
		Long timestamp,
		Long accountId
) {
}
