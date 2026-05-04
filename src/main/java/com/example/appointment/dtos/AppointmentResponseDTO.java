package com.example.appointment.dtos;

import com.example.appointment.models.Appointment;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponseDTO(
        UUID id,
        String clientName,
        String professionalName,
        String offeringName,
        BigDecimal price,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String status,
        UUID tenantId

) {public static AppointmentResponseDTO fromModel(Appointment appointment) {
    return new AppointmentResponseDTO(
            appointment.getId(),
            appointment.getClient().getName(),
            appointment.getProfessional().getName(),
            appointment.getOffering().getName(),
            appointment.getOffering().getPrice(),
            appointment.getStartTime(),
            appointment.getEndTime(),
            appointment.getStatus().name(),
            appointment.getTenant().getId()
        );
    }
}
