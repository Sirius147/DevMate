package com.sirius.DevMate.controller.dto.response;

import com.sirius.DevMate.domain.common.project.ApplicationStatus;
import com.sirius.DevMate.domain.common.project.Position;
import com.sirius.DevMate.domain.join.Application;

public record RecruitingProjectApplicationsDto(
        String nickname,
        Position applyPosition,
        String content,
        ApplicationStatus applicationStatus
) {
}
