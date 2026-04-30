package com.example.appointment.dtos;

import com.example.appointment.models.Offering;

import java.math.BigDecimal;

public record OfferingResponseDTO(
        String name,
        Integer durationMinutes,
        BigDecimal price
) {
    public static OfferingResponseDTO fromModel(Offering service) {
        return new OfferingResponseDTO(
                service.getName(),
                service.getDurationMinutes(),
                service.getPrice()
        );
    }
}
