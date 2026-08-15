package com.auction.notification_service.controller;


import com.auction.notification_service.dto.NotificationDto;
import com.auction.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping("/me")
    public List<NotificationDto> getMyNotifications(JwtAuthenticationToken token){
        UUID userId = UUID.fromString(token.getName());
        return notificationService.getMyNotifications(userId);
    }


    @PatchMapping("/{id}/read")
    public NotificationDto markAsRead(@PathVariable UUID id){
        return notificationService.markAsRead(id);
    }


}
