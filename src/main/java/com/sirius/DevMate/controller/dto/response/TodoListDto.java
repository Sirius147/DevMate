package com.sirius.DevMate.controller.dto.response;

import com.sirius.DevMate.domain.common.project.Position;
import com.sirius.DevMate.domain.common.project.Priority;

import java.time.Instant;
import java.time.LocalDate;

public record TodoListDto(
        Long toDoListId,
        String projectTitle,
        String title,
        Position position,
        String content,
        Priority priority,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isDone,
        Instant updatedAt
) {
}
