package com.auction.core_service.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionDto {

    private UUID id;

    private String title;

    private String description;

    private BigDecimal startingPrice;

    private BigDecimal currentPrice;

    private String status;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

    private UUID sellerId;

    private UUID winningBidId;

    private UUID categoryId;

    private UUID lastBidderId;

    private List<String> imageUrls;



}
