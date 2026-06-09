package com.example.expenseTracker.Service;

import com.example.expenseTracker.Entity.NotificationEntity;
import com.example.expenseTracker.Entity.ProfileEntity;
import com.example.expenseTracker.Repository.NotificationRepository;
import com.example.expenseTracker.Repository.ProfileRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProfileRepository profileRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            ProfileRepository profileRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.profileRepository = profileRepository;
    }

    private ProfileEntity getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return profileRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    public String createNotification(
            String title,
            String message
    ) {

        ProfileEntity user = getLoggedInUser();

        NotificationEntity notification =
                new NotificationEntity();

        notification.setTitle(title);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notification.setUser(user);

        notificationRepository.save(notification);

        return "Notification created";
    }

    public List<NotificationEntity> getMyNotifications() {

        ProfileEntity user = getLoggedInUser();

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(
                        user.getId()
                );
    }

    public String markAsRead(Long notificationId) {

        NotificationEntity notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                ));

        notification.setRead(true);

        notificationRepository.save(notification);

        return "Notification marked as read";
    }
}