package com.sirius.DevMate.domain.project.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_attachment")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chatAttachmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_id")
    private ChatMessage chatMessage;

    @Column(length = 300)
    private String url;

    @Column(length = 300)
    private String storageKey;

    @Column(length = 300)
    private String filename;

//    @Min(0)
//    @Max(10485760) // 최대 10MB
    @Column(columnDefinition = "BIGINT CHECK (file_size <= 10485760)")
    private Long fileSize;
}
