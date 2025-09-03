package com.sirius.DevMate.controller.dto.response;

import com.sirius.DevMate.domain.common.project.RequestMethod;

public record DocDto(
        Long docId,
        String projectTitle,
        String name,
        RequestMethod method,
        String path,
        String responseExample,
        String parameter
) {
}
