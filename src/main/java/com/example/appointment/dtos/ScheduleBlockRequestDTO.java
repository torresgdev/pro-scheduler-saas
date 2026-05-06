package com.example.appointment.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ScheduleBlockRequestDTO(
        @NotNull(message = "Início é obrigatório")
        LocalDateTime startTime,

        @NotNull(message = "Fim é obrigatório")
        LocalDateTime endTime,

        String reason // Ex: "Almoço", "Consulta Médica"
) {
}
