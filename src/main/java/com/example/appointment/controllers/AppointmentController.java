package com.example.appointment.controllers;


import com.example.appointment.dtos.AppointmentRequestDTO;
import com.example.appointment.dtos.AppointmentResponseDTO;
import com.example.appointment.services.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tenants/{tenantId}/appointments")
@Tag(name = "04. Gerenciamento de Marcação", description = "Gerencia um compromisso entre Cliente e profissional")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Cria um Compromisso de Serviço", description = "Cria um compromisso de prestação de serviço")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@PathVariable UUID tenantId,
                                                                    @RequestBody AppointmentRequestDTO requestDTO){
        var content = appointmentService.createAppointment(tenantId,requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(content);
    }


}
