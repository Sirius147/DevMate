package com.sirius.DevMate.controller.dto.chat;

import java.time.Instant;
import java.util.List;

public record ChatMessageDto(
        Long id,
        Long channelId,
        Long senderUserId,
        String messageType, // "TEXT"/"FILE"/"IMAGE"/"SYSTEM" 등
        String content,
        Instant createdAt,
        List<AttachmentDto> attachments
) {
    public record AttachmentDto(
            Long id,
            String url,
            String filename,
            String contentType,
            Long storageSize
    ){}
}
