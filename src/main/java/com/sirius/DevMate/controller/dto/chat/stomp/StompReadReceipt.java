package com.sirius.DevMate.controller.dto.chat.stomp;

public record StompReadReceipt(Long channelId, Long userId, Long lastReadMessageId) {
}
