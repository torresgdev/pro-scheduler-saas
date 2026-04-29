package com.example.appointment.controllers;

import com.example.appointment.dtos.TenantRequestDTO;
import com.example.appointment.dtos.TenantResponseDTO;
import com.example.appointment.dtos.TenantUpdateDTO;
import com.example.appointment.services.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor

@Tag(name = "01. Gerenciamento dos Tenants", description = "Crud completo de tudo que diz respeito aos tenants")
public class TenantController {
    private final TenantService service;

    @PostMapping
    @Operation(summary = "Cria novo Tenant", description = "Cria um novo Tenant com dados fornecidos")
    public ResponseEntity<TenantResponseDTO> createTenant(@RequestBody TenantRequestDTO dto){
        var result = service.createTenant(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Busca Tenant pelo ID", description = "Fornecendo um UUID válido irá voltar seu tenant respectivo")
    public ResponseEntity<TenantResponseDTO> findTenantById(@PathVariable UUID id) {
        var result = service.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    @Operation(summary = "Listar todos Tenants", description = "Lista todos os tenants cadastrados até o momento")
    public ResponseEntity<List<TenantResponseDTO>> listAllTenants() {
        var result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um Tenant", description = "Passando nome ou slug novo irá ser atualizado para o tenant do id representado")
    public ResponseEntity<TenantResponseDTO> updateTenant(@PathVariable UUID id,
                                                          @RequestBody TenantUpdateDTO dto) {
        var result = service.updateTenant(id,dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um Tenant", description = "Deleta um tenant com UUID fornecido")
    public ResponseEntity<Void> deleteTenantById(@PathVariable UUID id) {
        service.deleteTenant(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}
