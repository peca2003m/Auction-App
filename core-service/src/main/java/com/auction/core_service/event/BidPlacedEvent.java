package com.auction.core_service.event;


import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidPlacedEvent {

    private UUID auctionId;
    private UUID bidderId;
    private BigDecimal amount;
    private UUID previousBidderId;
    private String auctionTitle;


}
