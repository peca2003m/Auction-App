package com.auction.core_service.service;

import com.auction.core_service.dto.BidDto;
import com.auction.core_service.entity.Auction;
import com.auction.core_service.entity.Bid;
import com.auction.core_service.repository.AuctionRepository;
import com.auction.core_service.repository.BidRepository;
import lombok.RequiredArgsConstructor;
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


    @Transactional
    public BidDto placeBid(UUID auctionId, BigDecimal amount, UUID bidderId){


        Auction auction = auctionRepository.findByIdWithLock(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found!"));

        if (amount.compareTo(auction.getCurrentPrice()) <= 0) {
            throw new RuntimeException("Bid amount must be greater than current price!");
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
