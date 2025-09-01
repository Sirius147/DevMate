package com.sirius.DevMate.repository.user;

import com.sirius.DevMate.domain.user.Notification;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {

    private final EntityManager em;

    public Notification save(Notification notification) {
        if (notification.getNotificationId() == null) {
            em.persist(notification);
        } else {
            return em.merge(notification);
        }
        return notification;
    }
}

