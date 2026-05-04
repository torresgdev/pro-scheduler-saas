package com.example.appointment.controllers;


import com.example.appointment.dtos.AppointmentRequestDTO;
import com.example.appointment.dtos.AppointmentResponseDTO;
import com.example.appointment.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tenants/{tenantId}/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@PathVariable UUID tenantId,
                                                                    @RequestBody AppointmentRequestDTO requestDTO){
        var content = appointmentService.createAppointment(tenantId,requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(content);
    }


}
