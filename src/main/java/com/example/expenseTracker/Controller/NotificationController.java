package com.example.expenseTracker.controller;

import com.example.expenseTracker.Entity.NotificationEntity;
import com.example.expenseTracker.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @PostMapping("/create")
    public String createNotification(
            @RequestParam String title,
            @RequestParam String message
    ) {
        return notificationService
                .createNotification(title, message);
    }

    @GetMapping("/my")
    public List<NotificationEntity> getMyNotifications() {
        return notificationService.getMyNotifications();
    }

    @PutMapping("/read/{notificationId}")
    public String markAsRead(
            @PathVariable Long notificationId
    ) {
        return notificationService
                .markAsRead(notificationId);
    }
}