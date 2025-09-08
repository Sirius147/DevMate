package com.sirius.DevMate.controller.dto.response;

import com.sirius.DevMate.domain.common.project.CollaborateStyle;
import com.sirius.DevMate.domain.common.project.ProjectStatus;
import com.sirius.DevMate.domain.common.user.Regions;
import com.sirius.DevMate.domain.common.user.SkillLevel;
import com.sirius.DevMate.service.project.ProjectService;

import java.time.LocalDate;

public record AllProjectsResponseDto(
        Long projectId,
        String title,
        String shortDescription,
        Integer recruitSize,
        Integer currentSize,
        LocalDate startDate,
        LocalDate endDate,
        CollaborateStyle collaborateStyle,
        Regions preferredRegion,
        SkillLevel projectLevel,
        ProjectStatus projectStatus,
        Integer backendMembers,
        Integer currentBackend,
        Integer frontendMembers,
        Integer currentFrontend,
        Integer designMembers,
        Integer currentDesign,
        Integer pmMembers,
        Integer currentPm
) {
}
