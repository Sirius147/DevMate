package com.sirius.DevMate.config.security.controller.dto;

import com.sirius.DevMate.domain.common.user.StackType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StackSetUpDto {
    @NotNull
    @Size(min = 1, max = 20)
    private final String stackName;
    @NotNull
    private final StackType stackType;
}
