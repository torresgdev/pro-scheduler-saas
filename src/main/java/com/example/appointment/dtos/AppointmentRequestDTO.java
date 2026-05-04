package com.example.appointment.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequestDTO(
        UUID professionalId,
        UUID offeringId,
        LocalDateTime starTime,
        String clientName,
        String clientPhone

) {
}
