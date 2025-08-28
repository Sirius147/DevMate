package com.sirius.DevMate.config.security.controller.dto;

import com.sirius.DevMate.domain.common.user.Regions;
import com.sirius.DevMate.domain.common.user.SkillLevel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BasicSetUpDto {

    @NotNull
    @Size(min = 2, max = 20, message = "닉네임은 2~20자 여야 합니다")
    private final String nickname;
    @NotNull
    private final Regions regions;
    @NotNull
    private final SkillLevel skillLevel;
}
