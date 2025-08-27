package com.sirius.DevMate.config.security.controller.dto;

import com.sirius.DevMate.domain.common.user.Regions;
import com.sirius.DevMate.domain.common.user.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BasicSetUpDto {
    private final String nickname;
    private final Regions regions;
    private final SkillLevel skillLevel;
}
