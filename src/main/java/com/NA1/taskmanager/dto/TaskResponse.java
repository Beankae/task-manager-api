package com.NA1.taskmanager.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskResponse(
        Long id,
        String title,
        String description,
        Boolean completed,
        LocalDateTime createdAt
) {
}
