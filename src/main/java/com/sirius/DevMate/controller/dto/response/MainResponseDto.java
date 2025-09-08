package com.sirius.DevMate.controller.dto.response;


import com.sirius.DevMate.domain.project.Project;

public record MainResponseDto(
        PageList<AllProjectsResponseDto> pageList,
        boolean needNotify
) {
}
