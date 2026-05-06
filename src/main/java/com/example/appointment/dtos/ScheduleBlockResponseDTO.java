package com.example.appointment.dtos;

import com.example.appointment.models.ScheduleBlock;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleBlockResponseDTO(

        UUID id,
        UUID professionalId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String reason
) {
    public static ScheduleBlockResponseDTO fromModel(ScheduleBlock model) {
        return new ScheduleBlockResponseDTO(
                model.getId(),
                model.getProfessional().getId(),
                model.getStartTime(),
                model.getEndTime(),
                model.getReason()
        );
}}
