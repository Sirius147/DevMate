package com.sirius.DevMate.controller.dto.response;


import com.sirius.DevMate.domain.project.Project;

public record MainResponseDto(
        PageList<Project> pageList,
        boolean needNotify
) {
}
