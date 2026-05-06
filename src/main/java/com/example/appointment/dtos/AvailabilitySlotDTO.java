package com.example.appointment.dtos;

import java.time.LocalTime;

public record AvailabilitySlotDTO(
        LocalTime time,
        boolean available,
        String reason
) {
}
