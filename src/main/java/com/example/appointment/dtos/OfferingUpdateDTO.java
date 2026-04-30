package com.example.appointment.dtos;

import java.math.BigDecimal;
import java.util.Optional;

public record OfferingUpdateDTO(
        Optional<String> name,
        Optional<Integer> durationMinutes,
        Optional<BigDecimal> price
) {
}
