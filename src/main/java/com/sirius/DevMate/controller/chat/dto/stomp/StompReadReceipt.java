package com.sirius.DevMate.controller.chat.dto.stomp;

public record StompReadReceipt(Long channelId, Long userId, Long lastReadMessageId) {
}
