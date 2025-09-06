package com.sirius.DevMate.domain.project.chat;

import com.sirius.DevMate.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "chat_membership", uniqueConstraints = @UniqueConstraint(name = "uk_chat_membership",
        columnNames = {"chat_channel_id", "user_id"}))
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chatMembershipId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_channel_id")
    private ChatChannel chatChannel;

    @Column(nullable = false)
    private Instant lastReadAt; // Long lastReadChatMessageId

    @PrePersist
    public void prePersist() {
        this.lastReadAt = Instant.EPOCH; // 기본값 1970년
    }


}
