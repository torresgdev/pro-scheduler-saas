package com.example.appointment.services;

import com.example.appointment.dtos.OfferingRequestDTO;
import com.example.appointment.dtos.OfferingResponseDTO;
import com.example.appointment.exceptions.ExceptionConflict;
import com.example.appointment.exceptions.NotFoundExceptionT;
import com.example.appointment.models.Offering;
import com.example.appointment.repositories.OfferingRepository;
import com.example.appointment.repositories.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
