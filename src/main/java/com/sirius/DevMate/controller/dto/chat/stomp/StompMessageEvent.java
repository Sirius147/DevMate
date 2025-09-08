package com.sirius.DevMate.controller.dto.chat.stomp;

import java.time.Instant;
import java.util.List;

public record StompMessageEvent(Long id, Long channelId, Long senderUserId,
                                String messageType, String content, Instant createdAt,
                                List<Attachment> attachments) {
    public record Attachment(Long id, String url, String filename, String contentType, Long size){}
}
