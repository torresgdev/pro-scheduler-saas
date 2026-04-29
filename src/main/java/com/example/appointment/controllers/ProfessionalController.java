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
@RequestMapping("/professional")
@RequiredArgsConstructor
@Tag(name = "02. Gerenciamento de Profissionais", description = "crud completo dos profissionais")
public class ProfessionalController {

    private final ProfessionalService service;

    @PostMapping
    @Operation(summary = "Criar novo profissional", description = "Cria um novo profissional com dados inseridos corretamente")
    public ResponseEntity<ProfessionalResponseDTO> createProfessional(@RequestBody ProfessionalRequestDTO dto) {
        var result = service.createProfessional(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    @Operation(summary = "Lista Profissionais", description = "Lista todos os profissionais cadastrados na Empresa")
    public ResponseEntity<List<ProfessionalResponseDTO>> listAllProfessionalsByTenant(@PathVariable UUID tenantId) {
        var content = service.findAllByTenant(tenantId);
        return ResponseEntity.ok(content);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um profissional", description = "Busca profissional pelo id fornecido por Empresa")
    public ResponseEntity<ProfessionalResponseDTO> findProfessionalById(@PathVariable UUID id,
                                                                        @PathVariable UUID tenantId) {
        var content = service.findByIdAndTenantId(id, tenantId);
        return ResponseEntity.ok(content);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza profissional", description = "Atualiza um profissional com id fornecido e credenciais válidas")
    public ResponseEntity<ProfessionalResponseDTO> updateProfessionalByID(@PathVariable UUID id,
                                                                          @RequestBody ProfessionalUpdateDTO updateDTO) {
        var content = service.updateProfessional(id, updateDTO);
        return ResponseEntity.ok(content);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete um profissional", description = "Deleta profissional pelo id fornecido")
    public ResponseEntity<Void> deleteProfessionalById(@PathVariable UUID id) {
        service.deleteProfessional(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
