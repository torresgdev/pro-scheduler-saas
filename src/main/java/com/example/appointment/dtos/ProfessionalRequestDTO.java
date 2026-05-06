package com.example.appointment.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.UUID;

public record ProfessionalRequestDTO(
        @Schema(example = "Sandro Peixoto")
        String name,
        @Schema(example = "Profissional dedicado e experiente")
        String bio,
        @NotNull LocalTime workStartTime,

        @NotNull LocalTime workEndTime,
        UUID tenantId
) {
}
