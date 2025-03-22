package com.onseju.matchingservice.mapper;

import com.onseju.matchingservice.domain.TradeOrder;
import com.onseju.matchingservice.dto.OrderedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class EventMapper {

    public TradeOrder toTradeOrder(final OrderedEvent event) {
        return TradeOrder.builder()
                .id(event.id())
                .companyCode(event.companyCode())
                .type(event.type())
                .status(event.status())
                .totalQuantity(event.totalQuantity())
                .remainingQuantity(new AtomicReference<>(event.remainingQuantity()))
                .price(event.price())
                .createdDateTime(event.createdDateTime())
                .accountId(event.accountId())
                .build();
    }
}
