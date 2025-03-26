package com.onseju.matchingservice.events;

import java.util.List;

import com.onseju.matchingservice.dto.PriceLevelDto;

import lombok.Builder;

@Builder
public record OrderBookSyncedEvent(
		String companyCode,
		List<PriceLevelDto> sellLevels,
		List<PriceLevelDto> buyLevels
) {
}
