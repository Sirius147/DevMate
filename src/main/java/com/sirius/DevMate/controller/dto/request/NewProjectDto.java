package com.sirius.DevMate.controller.dto.request;

import com.sirius.DevMate.domain.common.project.CollaborateStyle;
import com.sirius.DevMate.domain.common.user.Regions;
import com.sirius.DevMate.domain.common.user.SkillLevel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record NewProjectDto(@NotNull @Size(min = 2, max = 20, message = "20글자 안쪽이어야 합니다") String title,
                            @NotNull @Size(min = 5, max = 300, message = "300글자 안쪽이어야 합니다") String shortDescription,
//                            @Positive @NotNull Integer recruitSize, @PositiveOrZero @NotNull Integer currentSize,
                            LocalDate startDate, LocalDate endDate, @NotNull CollaborateStyle collaborateStyle,
                            @NotNull Regions preferredRegion, @NotNull SkillLevel projectLevel,
                            @PositiveOrZero @NotNull Integer backendMembers,
                            @PositiveOrZero @NotNull Integer currentBackend,
                            @PositiveOrZero @NotNull Integer frontendMembers,
                            @PositiveOrZero @NotNull Integer currentFrontend,
                            @PositiveOrZero @NotNull Integer designMembers,
                            @PositiveOrZero @NotNull Integer currentDesign, @PositiveOrZero @NotNull Integer pmMembers,
                            @PositiveOrZero @NotNull Integer currentPm) {
}
