package com.auction.core_service.scheduler;

import com.auction.core_service.entity.Auction;
import com.auction.core_service.repository.AuctionRepository;
import com.auction.core_service.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionScheduler {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void closeExpiredAuctions() {


        List<Auction> expiredAuctions = auctionRepository.findByStatusAndEndsAtBefore("ACTIVE", LocalDateTime.now());

        for (Auction auction : expiredAuctions) {

            auction.setStatus("CLOSED");

            bidRepository.findFirstByAuctionIdOrderByAmountDesc(auction.getId())
                    .ifPresent(winningBid -> auction.setWinningBidId(winningBid.getId()));


            auctionRepository.save(auction);

        }


    }

}
