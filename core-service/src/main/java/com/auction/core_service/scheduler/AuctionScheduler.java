package com.auction.core_service.scheduler;

import com.auction.core_service.config.RabbitMQConfig;
import com.auction.core_service.entity.Auction;
import com.auction.core_service.entity.Bid;
import com.auction.core_service.event.AuctionClosedEvent;
import com.auction.core_service.repository.AuctionRepository;
import com.auction.core_service.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuctionScheduler {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void closeExpiredAuctions() {

        List<Auction> expiredAuctions = auctionRepository.findByStatusAndEndsAtBefore("ACTIVE", LocalDateTime.now());

        for (Auction auction : expiredAuctions) {

            auction.setStatus("CLOSED");

            Optional<Bid> winningBid = bidRepository.findFirstByAuctionIdOrderByAmountDesc(auction.getId());

            UUID winnerId = null;
            if (winningBid.isPresent()) {
                auction.setWinningBidId(winningBid.get().getId());
                winnerId = winningBid.get().getBidderId();
            }

            auctionRepository.save(auction);

            AuctionClosedEvent event = AuctionClosedEvent.builder()
                    .auctionId(auction.getId())
                    .sellerId(auction.getSellerId())
                    .winnerId(winnerId)
                    .auctionTitle(auction.getTitle())
                    .build();

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.AUCTION_EXCHANGE,
                    RabbitMQConfig.AUCTION_CLOSED_QUEUE,
                    event
            );
        }
    }
}