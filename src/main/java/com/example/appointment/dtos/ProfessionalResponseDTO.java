package com.example.appointment.dtos;

import com.example.appointment.models.Professional;

import java.util.UUID;

public record ProfessionalResponseDTO(
        UUID id,
        String name,
        String bio
) {
    public static ProfessionalResponseDTO fromModel(Professional profe) {
        return new ProfessionalResponseDTO(
                profe.getId(),
                profe.getName(),
                profe.getBio()
        );
    }
}
