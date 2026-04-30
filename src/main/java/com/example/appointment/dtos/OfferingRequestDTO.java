package com.example.appointment.dtos;

import java.math.BigDecimal;

public record OfferingRequestDTO(
        String name,
        Integer durationMinutes,
        BigDecimal price
) {
}
