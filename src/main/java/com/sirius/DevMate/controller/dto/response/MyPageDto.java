package com.sirius.DevMate.controller.dto.response;


import com.sirius.DevMate.domain.common.project.CollaborateStyle;
import com.sirius.DevMate.domain.common.project.Position;
import com.sirius.DevMate.domain.common.user.PreferredAtmosphere;
import com.sirius.DevMate.domain.common.user.Regions;
import com.sirius.DevMate.domain.common.user.SkillLevel;
import com.sirius.DevMate.domain.user.Stack;

import java.util.List;

public record MyPageDto(String name, String email, String nickname, String intro,
                        Regions region, SkillLevel skillLevel, Integer popularity,
                        PreferredAtmosphere preferredAtmosphere, CollaborateStyle collaborateStyle,
                        Position position, List<StackResponseDto> stackResponseDtos) {


}
