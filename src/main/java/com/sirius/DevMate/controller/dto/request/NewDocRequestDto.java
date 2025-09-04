package com.sirius.DevMate.controller.dto.request;

import com.sirius.DevMate.domain.common.project.RequestMethod;

public record NewDocRequestDto(
        String name,
        RequestMethod method,
        String path,
        String responseExample,
        String parameter
) {
}
