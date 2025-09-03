package com.sirius.DevMate.controller.dto.response;

import com.sirius.DevMate.domain.common.project.ApplicationStatus;

public record ApplicationResponseDto(
        Long id,
        String title,
        ApplicationStatus applicationStatus,
        String content) {
}
