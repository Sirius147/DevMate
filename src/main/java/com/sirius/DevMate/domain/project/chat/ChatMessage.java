package com.sirius.DevMate.domain.project.chat;

import com.sirius.DevMate.domain.common.BaseTimeEntity;
import com.sirius.DevMate.domain.common.sys.MessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_message", uniqueConstraints = @UniqueConstraint(name = "uk_chat_message", columnNames =
        {"chat_membership", "created_at"}))
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatMessage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chatMessageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_channel_id")
    private ChatChannel chatChannel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_membership_id")
    private ChatMembership chatMembership;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MessageType messageType;

    @Lob
    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "chatMessage", fetch = FetchType.LAZY) @Builder.Default
    private List<ChatAttachment> chatAttachments = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }

}
