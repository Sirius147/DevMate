package com.sirius.DevMate.controller.dto.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ReviewResponseDto(
        Integer projectStar,
        String content
) {
}
