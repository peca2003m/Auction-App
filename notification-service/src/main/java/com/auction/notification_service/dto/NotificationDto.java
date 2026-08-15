package com.auction.notification_service.dto;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {

    private UUID id;

    private String type;

    private String message;

    private Boolean isRead;

    private UUID userId;

    private UUID auctionId;



}
