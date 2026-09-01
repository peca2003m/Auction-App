package com.auction.notification_service.listener;

import com.auction.notification_service.entity.Notification;
import com.auction.notification_service.event.AuctionClosedEvent;
import com.auction.notification_service.event.BidPlacedEvent;
import com.auction.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationRepository notificationRepository;

    @RabbitListener(queues = "auction.bid.placed")
    public void onBidPlaced(BidPlacedEvent event) {
        Notification notification = Notification.builder()
                .type("OUTBID")
                .message("You have been outbid on \"" + event.getAuctionTitle() + "\"! New price: $" + event.getAmount())
                .isRead(false)
                .userId(event.getPreviousBidderId())
                .auctionId(event.getAuctionId())
                .createdAt(LocalDateTime.now())

                .build();

        if (event.getPreviousBidderId() != null) {
            notificationRepository.save(notification);
        }
    }

    @RabbitListener(queues = "auction.closed")
    public void onAuctionClosed(AuctionClosedEvent event) {
        if (event.getWinnerId() != null) {
            Notification winnerNotification = Notification.builder()
                    .type("AUCTION_WON")
                    .message("Congratulations! You won the auction!")
                    .isRead(false)
                    .userId(event.getWinnerId())
                    .auctionId(event.getAuctionId())
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(winnerNotification);
        }

        Notification sellerNotification = Notification.builder()
                .type("AUCTION_CLOSED")
                .message("Your auction has been closed!")
                .isRead(false)
                .userId(event.getSellerId())
                .auctionId(event.getAuctionId())
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(sellerNotification);
    }
}