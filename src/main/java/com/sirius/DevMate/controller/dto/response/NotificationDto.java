package com.sirius.DevMate.controller.dto.response;

import com.sirius.DevMate.domain.common.sys.NotificationType;

import java.time.Instant;

public record NotificationDto(NotificationType notificationType, String content, Instant createdAt) {

}
