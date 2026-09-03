package com.auction.core_service.repository;

import com.auction.core_service.entity.AuctionImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuctionImageRepository extends JpaRepository<AuctionImage, UUID> {
    List<AuctionImage> findByAuctionId(UUID auctionId);
}
