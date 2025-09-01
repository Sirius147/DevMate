package com.sirius.DevMate.controller.dto.request;

import com.sirius.DevMate.config.security.controller.dto.request.StackSetUpDto;
import com.sirius.DevMate.domain.common.project.CollaborateStyle;
import com.sirius.DevMate.domain.common.project.Position;
import com.sirius.DevMate.domain.common.user.Regions;
import com.sirius.DevMate.domain.common.user.SkillLevel;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateProfileDto(@Size(min = 5, max = 20) String nickname,
                               @Size(max = 100) String intro,
                               Regions regions,
                               SkillLevel skillLevel,
                               CollaborateStyle collaborateStyle,
                               Position position,
                               List<StackSetUpDto> stackSetUpDtos) {
}
