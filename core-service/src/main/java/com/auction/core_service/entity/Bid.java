package com.auction.core_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "bids")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "placed_at", nullable = false)
    private LocalDateTime placedAt;

    @Column(name = "bidder_id", nullable = false)
    private UUID bidderId;

    @ManyToOne
    @JoinColumn(name = "auction_id")
    private Auction auction;

}
