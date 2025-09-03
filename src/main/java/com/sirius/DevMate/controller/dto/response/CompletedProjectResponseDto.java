package com.sirius.DevMate.controller.dto.response;

import com.sirius.DevMate.domain.common.project.ProjectStatus;

public record CompletedProjectResponseDto(
        Long projectId,
        String title,
        String shortDescription,
        ProjectStatus projectStatus
) {
}
