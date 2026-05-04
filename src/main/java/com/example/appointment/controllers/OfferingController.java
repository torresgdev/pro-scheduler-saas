package com.example.appointment.controllers;

import com.example.appointment.dtos.OfferingRequestDTO;
import com.example.appointment.dtos.OfferingResponseDTO;
import com.example.appointment.dtos.OfferingUpdateDTO;
import com.example.appointment.services.OfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tenants/{tenantId}/offerings")
@RequiredArgsConstructor
public class OfferingController {

    private final OfferingService service;

    @PostMapping
    public ResponseEntity<OfferingResponseDTO> createOffering(@PathVariable UUID tenantId,
                                                              @RequestBody OfferingRequestDTO requestDTO){
        var result = service.createOffering(tenantId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping()
    public ResponseEntity<List<OfferingResponseDTO>> findAllOfferings(@PathVariable UUID tenantId) {
        var result = service.findAllOfferings(tenantId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{offeringId}")
    public ResponseEntity<OfferingResponseDTO> findOfferingById(@PathVariable UUID tenantId,
                                                                @PathVariable UUID offeringId){
        return ResponseEntity.ok(service.findOfferingById(tenantId,offeringId));
    }

    @PutMapping("/{offeringId}")
    public ResponseEntity<OfferingResponseDTO> updateOffering(@PathVariable UUID tenantId,
                                                              @PathVariable UUID offeringId,
                                                              @RequestBody OfferingUpdateDTO updateDTO){
        return ResponseEntity.ok(service.updateOffering(tenantId, offeringId, updateDTO));
    }

    @DeleteMapping("/{offeringId}")
    public ResponseEntity<Void> deleteOffering(@PathVariable UUID tenantId,
                                               @PathVariable UUID offeringId) {
        service.deleteOffering(tenantId, offeringId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
