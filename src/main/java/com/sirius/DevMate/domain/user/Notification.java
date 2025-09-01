package com.sirius.DevMate.domain.user;

import com.sirius.DevMate.domain.common.BaseTimeEntity;
import com.sirius.DevMate.domain.common.sys.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "notification")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationType notificationType;

    @Column(nullable = false, length = 300)
    private String content;

    private Instant readAt;

    @Column(nullable = false)
    private Boolean checked;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.checked = false;
    }

    public void checked() {
        this.checked = true;
    }
}
