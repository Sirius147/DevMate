package com.sirius.DevMate.controller.dto.request;

import com.sirius.DevMate.domain.common.project.Position;
import com.sirius.DevMate.domain.common.project.Priority;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record NewToDoListRequestDto(
        String title,
        Position position,
        @Size(min = 5, max = 200) String content,
        Priority priority,
        LocalDate startDate,
        LocalDate endDate,
        Boolean done

) {
}
