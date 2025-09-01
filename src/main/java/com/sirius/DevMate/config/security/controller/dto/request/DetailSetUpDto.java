package com.sirius.DevMate.config.security.controller.dto.request;

import com.sirius.DevMate.domain.common.project.CollaborateStyle;
import com.sirius.DevMate.domain.common.project.Position;
import com.sirius.DevMate.domain.common.user.PreferredAtmosphere;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DetailSetUpDto {
    @NotNull
    private final PreferredAtmosphere preferredAtmosphere;
    @NotNull
    private final CollaborateStyle collaborateStyle;
    @NotNull
    private final Position position;
}
