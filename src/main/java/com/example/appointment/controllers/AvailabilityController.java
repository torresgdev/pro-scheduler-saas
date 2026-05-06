package com.example.appointment.controllers;

import com.example.appointment.dtos.AvailabilitySlotDTO;
import com.example.appointment.services.AvailabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tenants/{tenantId}/availability")
@Tag(name = "06. Disponibilidade", description = "Consulta de horários livres para agendamento")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @GetMapping("/professionals/{professionalId}")
    @Operation(summary = "Consulta horários disponíveis",
            description = "Retorna uma lista de slots (blocos) de tempo informando o que está livre ou ocupado")
    public ResponseEntity<List<AvailabilitySlotDTO>> getAvailability(
            @PathVariable UUID tenantId,
            @PathVariable UUID professionalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        // O tenantId aqui serve para validar se o profissional pertence à empresa antes de listar
        var availability = availabilityService.getAvailability(tenantId, professionalId, date);
        return ResponseEntity.ok(availability);
    }
}