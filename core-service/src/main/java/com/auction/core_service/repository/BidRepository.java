package com.auction.core_service.repository;

import com.auction.core_service.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {

    List<Bid> findByAuctionId(UUID auctionId);

    List<Bid> findByBidderId(UUID bidderId);

    Optional<Bid> findFirstByAuctionIdOrderByAmountDesc(UUID auctionId);

}
