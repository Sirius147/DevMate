package com.sirius.DevMate.controller.dto.response;


import com.sirius.DevMate.domain.common.project.ProjectStatus;

public record RecruitingProjectResponseDto(
        Long ProjectId,
        String title,
        Integer recruitSize,
        Integer currentSize,
        Integer backendMembers,
        Integer currentBackend,
        Integer frontendMembers,
        Integer currentFrontend,
        Integer designMembers,
        Integer currentDesign,
        Integer pmMembers,
        Integer currentPm,
        ProjectStatus projectStatus
) {
}
