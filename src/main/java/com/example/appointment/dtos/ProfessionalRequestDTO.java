package com.example.appointment.dtos;

import java.util.UUID;

public record ProfessionalRequestDTO(
        String name,
        String bio,
        UUID tenantId
) {
}
