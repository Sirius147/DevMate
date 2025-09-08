package com.sirius.DevMate.controller.dto.chat.stomp;

import java.util.List;

public record StompSendMessage(
        String content,
        List<AttachmentPayload> attachmentPayloads
        ) {
    public record AttachmentPayload(
            String storageKey, String url, String filename, String contentType, Long fileSize
    ) {
    }
}
