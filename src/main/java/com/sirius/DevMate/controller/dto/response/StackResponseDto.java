package com.sirius.DevMate.controller.dto.response;

import com.sirius.DevMate.domain.common.user.StackType;

public record StackResponseDto(
        StackType stackType,
        String stackName
) {
}
