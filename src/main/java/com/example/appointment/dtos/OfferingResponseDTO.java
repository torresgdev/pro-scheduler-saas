package com.example.appointment.dtos;

import com.example.appointment.models.Offering;

import java.math.BigDecimal;
import java.util.UUID;

public record OfferingResponseDTO(
        UUID id,
        String name,
        Integer durationMinutes,
        BigDecimal price
) {
    public static OfferingResponseDTO fromModel(Offering service) {
        return new OfferingResponseDTO(
                service.getId(),
                service.getName(),
                service.getDurationMinutes(),
                service.getPrice()
        );
    }
}
