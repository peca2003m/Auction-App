package com.auction.core_service.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidDto {


    private UUID id;

    private BigDecimal amount;

    private LocalDateTime placedAt;

    private UUID bidderId;

    private UUID auctionId;



}
