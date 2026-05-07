package com.example.appointment.controllers;

import com.example.appointment.dtos.ScheduleBlockRequestDTO;
import com.example.appointment.dtos.ScheduleBlockResponseDTO;
import com.example.appointment.services.AppointmentService;
import com.example.appointment.services.ScheduleBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tenants/{tenantId}/professionals/{professionalId}/schedule-blocks")
@Tag(name = "05. Bloqueios de Agenda", description = "Gerencia pausas, almoços e folgas do profissional")
public class ScheduleBlockController {

    private final ScheduleBlockService scheduleBlockService;

    @PostMapping
    @Operation(summary = "Cria um bloqueio", description = "Bloqueia um intervalo da agenda do profissional")
    public ResponseEntity<ScheduleBlockResponseDTO> createBlock(
            @PathVariable UUID tenantId,
            @PathVariable UUID professionalId,
            @Valid @RequestBody ScheduleBlockRequestDTO requestDTO) {

        var content = scheduleBlockService.createBlock(tenantId, professionalId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(content);
    }

    @GetMapping
    @Operation(summary = "Lista bloqueios", description = "Retorna todos os bloqueios de um profissional específico")
    public ResponseEntity<List<ScheduleBlockResponseDTO>> getBlocks(
            @PathVariable UUID tenantId,
            @PathVariable UUID professionalId) {
        return ResponseEntity.ok(scheduleBlockService.findAllByProfessional(tenantId, professionalId));
    }

    @DeleteMapping("/{scheduleBlockId}")
    @Operation(summary = "Deleta um bloqueio", description = "Deleta um bloqueio assim que preciso, passando o ID do bloqueio")
    public ResponseEntity<Void> blockDelete(@PathVariable UUID tenantId,
                                            @PathVariable UUID professionalId,
                                            @PathVariable UUID scheduleBlockId) {
        scheduleBlockService.deleteBlock(tenantId,professionalId,scheduleBlockId);
        return ResponseEntity.noContent().build();
    }
}
