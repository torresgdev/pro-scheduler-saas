package com.example.appointment.services;

import com.example.appointment.dtos.OfferingRequestDTO;
import com.example.appointment.dtos.OfferingResponseDTO;
import com.example.appointment.dtos.OfferingUpdateDTO;
import com.example.appointment.exceptions.ExceptionConflict;
import com.example.appointment.exceptions.NotFoundExceptionT;
import com.example.appointment.models.Offering;
import com.example.appointment.repositories.OfferingRepository;
import com.example.appointment.repositories.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferingService {

    private final OfferingRepository offeringRepository;
    private final TenantRepository tenantRepository;


    public OfferingResponseDTO createService(UUID tenantId, OfferingRequestDTO requestDTO) {

        var tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new NotFoundExceptionT("Tenant não existe," +
                " tente novamente."));

        if (offeringRepository.existsByNameAndTenantId(requestDTO.name(), tenantId)) {
            throw new ExceptionConflict("Nome de serviço já cadastrado!");
        }


        Offering offering = new Offering(
                requestDTO.name(),
                requestDTO.durationMinutes(),
                requestDTO.price(),
                tenant
        );

        Offering saved = offeringRepository.save(offering);
        return OfferingResponseDTO.fromModel(saved);
    }


    public List<OfferingResponseDTO> findAllOfferings(UUID tenantId) {
        if (!tenantRepository.existsById(tenantId)) {
            throw new NotFoundExceptionT("Tenant não encontrado, tente novamente");
        }

        return offeringRepository.findAllByTenantId(tenantId).stream().map(OfferingResponseDTO::fromModel).toList();
    }

    public OfferingResponseDTO findOfferingById(UUID tenantId, UUID offeringId) {
        if (!tenantRepository.existsById(tenantId)) {
            throw new NotFoundExceptionT("Tenant não encontrado");
        }

        Offering offering = offeringRepository.findByTenantIdAndId(tenantId, offeringId).orElseThrow(() ->
                new NotFoundExceptionT("Serviço não encontrado ou não pertence a esta empresa"));

        return OfferingResponseDTO.fromModel(offering);
    }

    public OfferingResponseDTO updateOffering(UUID tenantId, UUID offeringId, OfferingUpdateDTO updateDTO) {
        Offering offeringToUpdate = offeringRepository.findByTenantIdAndId(tenantId, offeringId).orElseThrow(() ->
                new NotFoundExceptionT("Tenant não encontrado ou Serviço não pertence a esta empresa."));


        updateDTO.name().ifPresent(newName -> {
            if (!offeringToUpdate.getName().equals(newName)) {

                if (offeringRepository.existsByNameAndTenantId(newName, tenantId)){
                    throw new ExceptionConflict("Já existe outro serviço com este nome neste Tenant.");
                }
                offeringToUpdate.setName(newName);
            }
        });
        updateDTO.durationMinutes().ifPresent(offeringToUpdate::setDurationMinutes);
        updateDTO.price().ifPresent(offeringToUpdate::setPrice);

        Offering saved = offeringRepository.save(offeringToUpdate);

        return OfferingResponseDTO.fromModel(saved);
    }

    public void deleteOffering(UUID tenantId, UUID offeringId) {
        Offering offering = offeringRepository.findByTenantIdAndId(tenantId, offeringId).orElseThrow(() -> new NotFoundExceptionT("Serviço não encontrado"));

        offeringRepository.delete(offering);

    }
}
