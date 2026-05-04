package com.example.appointment.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record OfferingRequestDTO(
        @Schema(example = "Corte Simples")
        String name,
        @Schema(example = "30")
        Integer durationMinutes,
        @Schema(example = "45")
        BigDecimal price
) {
}
