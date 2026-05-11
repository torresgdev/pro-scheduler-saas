package com.example.appointment.controllers;


import com.example.appointment.dtos.AppointmentRequestDTO;
import com.example.appointment.dtos.AppointmentResponseDTO;
import com.example.appointment.models.enums.PaymentMethod;
import com.example.appointment.services.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tenants/{tenantId}/appointments")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "04. Gerenciamento de Marcação", description = "Gerencia um compromisso entre Cliente e profissional")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Cria um Compromisso de Serviço", description = "Cria um compromisso de prestação de serviço")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@Valid @PathVariable UUID tenantId,
                                                                    @RequestBody AppointmentRequestDTO requestDTO){
        var content = appointmentService.createAppointment(tenantId,requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(content);
    }

    @GetMapping("/{professionalId}")
    @Operation(summary = "Lista horarios agendados", description = "lista todos os horarios agendados do profissional da empresa")
    public ResponseEntity<List<AppointmentResponseDTO>> listAllAppointments(@Valid @PathVariable UUID tenantId,
                                                                         @PathVariable UUID professionalId){
        var content = appointmentService.listAllAppointments(tenantId,professionalId);
        return ResponseEntity.ok(content);
    }

    @GetMapping("/filter/{professionalId}")
    @Operation(summary = "Lista horarios agendados do dia", description = "lista todos os horarios agendados do profissional apenas do dia")
    public ResponseEntity<List<AppointmentResponseDTO>> listAllAppointmentsByProfessionalByTimeRange(@Valid @PathVariable UUID tenantId,
                                                                                                     @PathVariable UUID professionalId,
                                                                                                     @RequestParam(required = false)
                                                                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){

        LocalDate targetDate = (date != null) ? date : LocalDate.now();

        var content = appointmentService.listAllAppointmentsByDay(tenantId,professionalId, targetDate);
        return ResponseEntity.ok(content);
    }

    @PatchMapping("/{appointmentId}/complete")
    @Operation(summary = "Finaliza um agendamento", description = "Marca o status como COMPLETED e gera uma transação financeira")
    public ResponseEntity<Void> completeAppointment(@PathVariable UUID tenantId,
                                                    @PathVariable UUID appointmentId,
                                                    @RequestParam PaymentMethod paymentMethod) {
        appointmentService.completeAppointment(tenantId,appointmentId,paymentMethod);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/today")
    @Operation(summary = "Busca todos os agendamentos  do dia", description = "Busca todos agendamentos marcados para o dia em questão")
    public ResponseEntity<List<AppointmentResponseDTO>> listAllAppointmentsByToday(@PathVariable UUID tenantId) {
        var result = appointmentService.getTodayQueue(tenantId);
        return ResponseEntity.ok().body(result);
    }



}
