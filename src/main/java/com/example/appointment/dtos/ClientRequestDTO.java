package com.example.appointment.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClientRequestDTO(
        @Schema(example = "Carlos Souto")
        String name,
        @Schema(example = "21980687804")
        String phone
) {
}
