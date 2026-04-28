package com.example.appointment.services;

import com.example.appointment.dtos.TenantRequestDTO;
import com.example.appointment.dtos.TenantResponseDTO;
import com.example.appointment.exceptions.ExceptionConflict;
import com.example.appointment.exceptions.NotFoundExceptionT;
import com.example.appointment.models.Tenant;
import com.example.appointment.repositories.TentantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantService {

    public final TentantRepository repository;

// CREATE NEW TENANT
    public TenantResponseDTO createTenant(TenantRequestDTO requestDTO) {
        if (repository.findBySlug(requestDTO.slug()).isPresent()) {
            throw new ExceptionConflict("Nome ja cadastrado");
        }

        Tenant tenant = new Tenant(requestDTO.name(), requestDTO.slug());
        repository.save(tenant);

        return TenantResponseDTO.fromEntity(tenant);
    }

    // FIND BY ID TENANT
    public TenantResponseDTO findById(UUID id) {
        Tenant tenant = repository.findById(id).orElseThrow(() -> new NotFoundExceptionT("Não encontrado, Revise os dados inseridos"));
        return TenantResponseDTO.fromEntity(tenant);
    }

    // LIST ALL TENANTS
    public List<TenantResponseDTO> findAll() {

    }



}
