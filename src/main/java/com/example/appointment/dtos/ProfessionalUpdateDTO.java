package com.example.appointment.dtos;

import java.time.LocalTime;
import java.util.Optional;

public record ProfessionalUpdateDTO(
        Optional<String> name,
        Optional<String> bio,
        Optional<LocalTime> start,
        Optional<LocalTime> end
) {
}
