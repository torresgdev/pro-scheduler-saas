package com.example.appointment.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequestDTO(
        UUID professionalId,
        UUID offeringId,
        LocalDateTime starTime,
        @Schema(example = "Antonio Carlos")
        String clientName,
        @Schema(example = "21984574514")
        String clientPhone

) {
}
