package com.example.appointment.services;


import com.example.appointment.dtos.ProfessionalRequestDTO;
import com.example.appointment.dtos.ProfessionalResponseDTO;
import com.example.appointment.dtos.ProfessionalUpdateDTO;
import com.example.appointment.exceptions.NotFoundExceptionT;
import com.example.appointment.models.Professional;
import com.example.appointment.repositories.ProfessionalRepository;
import com.example.appointment.repositories.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final TenantRepository tenantRepository;


    // CREATE PROFESSIONAL
    public ProfessionalResponseDTO createProfessional(ProfessionalRequestDTO requestDTO) {
        var tenant = tenantRepository.findById(requestDTO.tenantId()).orElseThrow(() -> new NotFoundExceptionT("Tenant não encontrado para vincular o profissional"));
        Professional professional = new Professional(
                requestDTO.name(),
                requestDTO.bio(),
                tenant
        );


        professionalRepository.save(professional);

        return ProfessionalResponseDTO.fromModel(professional);
    }

    // FIND ALL PROFESSIONAL
    public List<ProfessionalResponseDTO> findAllByTenant(UUID tenantId) {
        if (!tenantRepository.existsById(tenantId)) {
            throw new NotFoundExceptionT("Empresa (tenant) não encontrada");
        }
        return professionalRepository.findAllByTenantId(tenantId)
                .stream()
                .map(ProfessionalResponseDTO::fromModel)
                .toList();
    }

    //FIND BY ID PROFESSIONAL
   public ProfessionalResponseDTO findByIdAndTenantId(UUID id, UUID tenantId) {
        Professional professional = professionalRepository.findByIdAndTenantId(id, tenantId).orElseThrow(() -> new
                NotFoundExceptionT("Profissional não encontrado para essa empresa"));
        return ProfessionalResponseDTO.fromModel(professional);
   }
    //Update PRofessional by ID
    public ProfessionalResponseDTO updateProfessional(UUID id, ProfessionalUpdateDTO updateDTO) {
        Professional professionalToUpdate = professionalRepository.findById(id).orElseThrow(() -> new NotFoundExceptionT("ID não encontrado" +
                "tente novamente com ID válido."));

        updateDTO.name().ifPresent(professionalToUpdate::setName);
        updateDTO.bio().ifPresent(professionalToUpdate::setBio);
        professionalRepository.save(professionalToUpdate);

        return ProfessionalResponseDTO.fromModel(professionalToUpdate);
    }

    //Delete PRofessional
    public void deleteProfessional(UUID id) {
        Professional professional = professionalRepository.findById(id).orElseThrow(() -> new NotFoundExceptionT("ID não encontrado" +
                "tente novamente com ID válido."));
        professionalRepository.delete(professional);
    }
}
