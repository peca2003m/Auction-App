package com.auction.core_service.event;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionClosedEvent {

    private UUID auctionId;
    private UUID winnerId;
    private UUID sellerId;
    private String auctionTitle;

}
