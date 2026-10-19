package com.auction.core_service.kafka;

import com.auction.core_service.event.BidKafkaEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class BidHistoryConsumer {

    private final Map<UUID, List<BidKafkaEvent>> bidHistory = new ConcurrentHashMap<>();

    @KafkaListener(topics = "auction-bid-events", groupId = "auction-group")
    public void consume(BidKafkaEvent event) {
        log.info("Kafka received bid event for auction: {}", event.getAuctionId());
        bidHistory
                .computeIfAbsent(event.getAuctionId(), k -> new ArrayList<>())
                .add(event);
    }

    public List<BidKafkaEvent> getHistory(UUID auctionId) {
        return bidHistory.getOrDefault(auctionId, new ArrayList<>());
    }
}