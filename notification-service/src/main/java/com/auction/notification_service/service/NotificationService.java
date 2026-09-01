package com.auction.notification_service.service;


import com.auction.notification_service.dto.NotificationDto;
import com.auction.notification_service.entity.Notification;
import com.auction.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;


    public List<NotificationDto> getMyNotifications(UUID userId){

        return notificationRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();

    }

    public NotificationDto markAsRead(UUID id){
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(true);
        notificationRepository.save(notification);

        return mapToDto(notification);

    }


    private NotificationDto mapToDto(Notification notification){

        return NotificationDto.builder()
                .id(notification.getId())
                .type(notification.getType())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .userId(notification.getUserId())
                .auctionId(notification.getAuctionId())
                .createdAt(notification.getCreatedAt())
                .build();

    }


}
