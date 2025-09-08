package com.sirius.DevMate.controller.dto.request;

import com.sirius.DevMate.domain.common.project.CollaborateStyle;
import com.sirius.DevMate.domain.common.project.ProjectStatus;
import com.sirius.DevMate.domain.common.user.PreferredAtmosphere;
import com.sirius.DevMate.domain.common.user.Regions;
import com.sirius.DevMate.domain.common.user.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSearchRequestDto {

    private Integer page;
    private Regions regions;
    private CollaborateStyle collaborateStyle;
    private SkillLevel skillLevel;
    private ProjectStatus projectStatus;

}
