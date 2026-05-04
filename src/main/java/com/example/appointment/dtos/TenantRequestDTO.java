package com.example.appointment.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record TenantRequestDTO(
        @Schema(example = "Barbearia do Cosme")
        String name,
        @Schema(example = "barber-cosme")
        String slug
) {
}
