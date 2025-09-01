package com.sirius.DevMate.controller.dto.response;


import java.util.List;

public record PageList<T>(
        List<T> content,
        Long totalCount
) {
}
