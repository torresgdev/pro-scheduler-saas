package com.example.appointment.controllers;


import com.example.appointment.dtos.ProfessionalRequestDTO;
import com.example.appointment.dtos.ProfessionalResponseDTO;
import com.example.appointment.dtos.ProfessionalUpdateDTO;
import com.example.appointment.services.ProfessionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tenants/{tenantId}/professionals")
@RequiredArgsConstructor
@Tag(name = "02. Gerenciamento de Profissionais", description = "crud completo dos profissionais v1")
public class ProfessionalController {

    private final ProfessionalService service;

    @PostMapping
    @Operation(summary = "Criar novo profissional", description = "Cria um novo profissional com dados inseridos corretamente")
    public ResponseEntity<ProfessionalResponseDTO> createProfessional(@PathVariable UUID tenantId,
                                                                      @RequestBody ProfessionalRequestDTO dto) {
        var result = service.createProfessional(tenantId,dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    @Operation(summary = "Lista profissionais de um Tenant")
    public ResponseEntity<List<ProfessionalResponseDTO>> listAll(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(service.findAllByTenant(tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca profissional específico de um Tenant")
    public ResponseEntity<ProfessionalResponseDTO> getById(
            @PathVariable UUID tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(service.findByIdAndTenantId(id, tenantId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza profissional", description = "Atualiza um profissional com id fornecido e credenciais válidas")
    public ResponseEntity<ProfessionalResponseDTO> updateProfessionalByID(@PathVariable UUID id,
                                                                          @PathVariable UUID tenantId,
                                                                          @RequestBody ProfessionalUpdateDTO updateDTO) {
        var content = service.updateProfessional(id, tenantId, updateDTO);
        return ResponseEntity.ok(content);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete um profissional", description = "Deleta profissional pelo id fornecido")
    public ResponseEntity<Void> deleteProfessionalById(@PathVariable UUID tenantId,
                                                       @PathVariable UUID id) {
        service.deleteProfessional(tenantId,id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
