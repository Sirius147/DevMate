package com.sirius.DevMate.controller.dto.response;

import com.sirius.DevMate.domain.common.project.MembershipRole;
import com.sirius.DevMate.domain.common.project.MembershipStatus;
import com.sirius.DevMate.domain.common.project.ProjectStatus;

public record MembershipDto(
        String title,
        ProjectStatus projectStatus,
        MembershipStatus membershipStatus,
        MembershipRole membershipRole) {
}
