package com.auction.core_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "auction_images")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "order_index")
    private Integer orderIndex;

    @ManyToOne
    @JoinColumn(name = "auction_id")
    private Auction auction;


}
