package com.sirius.DevMate.config.security.controller.dto;

import com.sirius.DevMate.domain.common.user.Regions;
import com.sirius.DevMate.domain.common.user.SkillLevel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BasicSetUpDto {

    @NotNull
    @Size(min = 2, max = 20, message = "닉네임은 2~20자 여야 합니다")
    private String nickname;
    @NotNull
    private Regions regions;
    @NotNull
    private SkillLevel skillLevel;
}
