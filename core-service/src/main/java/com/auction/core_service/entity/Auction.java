package com.auction.core_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "auctions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "starting_price", nullable = false)
    private BigDecimal startingPrice;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "starts_at")
    private LocalDateTime startsAt;

    @Column(name = "ends_at")
    private LocalDateTime endsAt;

    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;

    @Column(name = "winning_bid_id")
    private UUID winningBidId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    private List<Bid> bids;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    private List<AuctionImage> images;
}
