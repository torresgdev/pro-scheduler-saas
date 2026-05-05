package com.example.appointment.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequestDTO(
        UUID professionalId,
        UUID offeringId,
        @NotNull(message = "Hora de inicio do serviço não pode estar em branco")
        @Future(message = "O agendamento deve ser para uma data futura")
        LocalDateTime starTime,
        @NotBlank(message = "Nome do cliente não pode estar em branco")
        @Schema(example = "Antonio Carlos")
        String clientName,
        @Schema(example = "21984574514")
        @NotBlank(message = "Telefone do cliente não pode estar em branco")
        String clientPhone

) {
}
