package com.sirius.DevMate.controller.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ReviewContentDto(
        @Min(1) @Max(5) Integer star,
        @Size(min = 10, max = 200) String content
) {
}
