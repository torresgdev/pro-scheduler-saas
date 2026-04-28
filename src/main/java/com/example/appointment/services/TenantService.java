package com.example.appointment.services;

import com.example.appointment.dtos.TenantRequestDTO;
import com.example.appointment.dtos.TenantResponseDTO;
import com.example.appointment.dtos.TenantUpdateDTO;
import com.example.appointment.exceptions.ExceptionConflict;
import com.example.appointment.exceptions.NotFoundExceptionT;
import com.example.appointment.models.Tenant;
import com.example.appointment.repositories.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantService {

    public final TenantRepository repository;

// CREATE NEW TENANT
    public TenantResponseDTO createTenant(TenantRequestDTO requestDTO) {
        if (repository.findBySlug(requestDTO.slug()).isPresent()) {
            throw new ExceptionConflict("Slug já cadastrado");
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
       List<Tenant> tenants = repository.findAll();

       if (tenants.isEmpty()) {
           throw new NotFoundExceptionT("Nennhum tenant encontrado nessa lista");
       }

       return tenants.stream().map(TenantResponseDTO::fromEntity).toList();
    }


    // UPDATE TENANTS
    public TenantResponseDTO updateTenant(UUID id, TenantUpdateDTO updateDTO) {
        Tenant tenantToUpdate = repository.findById(id).orElseThrow(() -> new NotFoundExceptionT("Não encontrado, Revise os dados inseridos"));

        //Atualiza o nome se tiver
        updateDTO.name().ifPresent(tenantToUpdate::setName);


        // validação do slug não pode ser o mesmo ou ja ter no banco
        updateDTO.slug().ifPresent(newSlug -> {
            if (!newSlug.equals(tenantToUpdate.getSlug()) && repository.existsBySlug(newSlug)) {
                throw new ExceptionConflict("Slug já cadastrado");
            }
            //salva o slug novo
            tenantToUpdate.setSlug(newSlug);
        });

        repository.save(tenantToUpdate);
        return TenantResponseDTO.fromEntity(tenantToUpdate);
    }

    // DELETE TENANTS
    public void deleteTenant(UUID id) {
        Tenant tenant = repository.findById(id).orElseThrow(() -> new NotFoundExceptionT("Não encontrado, Revise os dados inseridos"));
        repository.delete(tenant);
    }



}
