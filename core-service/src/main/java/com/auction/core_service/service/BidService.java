package com.auction.core_service.service;

import com.auction.core_service.config.RabbitMQConfig;
import com.auction.core_service.dto.BidDto;
import com.auction.core_service.entity.Auction;
import com.auction.core_service.entity.Bid;
import com.auction.core_service.event.BidPlacedEvent;
import com.auction.core_service.repository.AuctionRepository;
import com.auction.core_service.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final RabbitTemplate rabbitTemplate;


    @Transactional
    public BidDto placeBid(UUID auctionId, BigDecimal amount, UUID bidderId){


        Auction auction = auctionRepository.findByIdWithLock(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found!"));

        if (amount.compareTo(auction.getCurrentPrice()) <= 0) {
            throw new RuntimeException("Bid amount must be greater than current price!");
        }

        if (!auction.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("Auction is not active!");
        }

        auction.setCurrentPrice(amount);

        Bid bid = Bid.builder()
                .amount(amount)
                .placedAt(LocalDateTime.now())
                .bidderId(bidderId)
                .auction(auction)
                .build();

        bidRepository.save(bid);
        auctionRepository.save(auction);


        BidPlacedEvent event = BidPlacedEvent.builder()
                .auctionId(auctionId)
                .bidderId(bidderId)
                .amount(amount)
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.AUCTION_EXCHANGE,
                RabbitMQConfig.BID_PLACED_QUEUE,
                event
        );


        return mapToDto(bid);
    }


    private BidDto mapToDto(Bid bid){

        return BidDto.builder()
                .id(bid.getId())
                .amount(bid.getAmount())
                .placedAt(bid.getPlacedAt())
                .bidderId(bid.getBidderId())
                .auctionId(bid.getAuction() != null ? bid.getAuction().getId() : null)
                .build();

    }

}
