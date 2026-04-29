package com.example.appointment.dtos;

import java.util.Optional;

public record ProfessionalUpdateDTO(
        Optional<String> name,
        Optional<String> bio
) {
}
