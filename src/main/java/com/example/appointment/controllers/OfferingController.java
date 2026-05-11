package com.example.appointment.controllers;

import com.example.appointment.dtos.OfferingRequestDTO;
import com.example.appointment.dtos.OfferingResponseDTO;
import com.example.appointment.dtos.OfferingUpdateDTO;
import com.example.appointment.services.OfferingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/tenants/{tenantId}/offerings")
@RequiredArgsConstructor
@Tag(name = "03. Gerenciamento de Serviços", description = "crud completo de serviços prestados")
public class OfferingController {

    private final OfferingService service;

    @PostMapping
    @Operation(summary = "Cria novo Serviço", description = "Cria um novo serviço com dados fornecidos")
    public ResponseEntity<OfferingResponseDTO> createOffering(@PathVariable UUID tenantId,
                                                              @RequestBody OfferingRequestDTO requestDTO){
        var result = service.createOffering(tenantId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping()
    @Operation(summary = "Busca todos os serviços", description = "Busca todos servicos que podem ser prestados por esse tenant(empresa)")
    public ResponseEntity<List<OfferingResponseDTO>> findAllOfferings(@PathVariable UUID tenantId) {
        var result = service.findAllOfferings(tenantId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{offeringId}")
    @Operation(summary = "Busca serviço pelo Id", description = "Busca todos servico pelo Id que pode ser prestado  por esse tenant(empresa)")
    public ResponseEntity<OfferingResponseDTO> findOfferingById(@PathVariable UUID tenantId,
                                                                @PathVariable UUID offeringId){
        return ResponseEntity.ok(service.findOfferingById(tenantId,offeringId));
    }

    @PutMapping("/{offeringId}")
    @Operation(summary = "Atualiza um Serviço", description = "Atualiza um serviço pelo Id fornecido que pode ser prestado por esse tenant(empresa)")
    public ResponseEntity<OfferingResponseDTO> updateOffering(@PathVariable UUID tenantId,
                                                              @PathVariable UUID offeringId,
                                                              @RequestBody OfferingUpdateDTO updateDTO){
        return ResponseEntity.ok(service.updateOffering(tenantId, offeringId, updateDTO));
    }

    @DeleteMapping("/{offeringId}")
    @Operation(summary = "Deleta(soft delete) um Serviço", description = "Deleta um serviço, softdelete apenas seta como inativo e não aparece mais nas pesquisas")
    public ResponseEntity<Void> deleteOffering(@PathVariable UUID tenantId,
                                               @PathVariable UUID offeringId) {
        service.deleteOffering(tenantId, offeringId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
