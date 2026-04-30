package com.example.appointment;


import com.example.appointment.dtos.OfferingRequestDTO;
import com.example.appointment.dtos.OfferingResponseDTO;
import com.example.appointment.exceptions.ExceptionConflict;
import com.example.appointment.exceptions.NotFoundExceptionT;
import com.example.appointment.models.Offering;
import com.example.appointment.models.Tenant;
import com.example.appointment.repositories.OfferingRepository;
import com.example.appointment.repositories.TenantRepository;
import com.example.appointment.services.OfferingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OfferingServiceTest {

    @Mock
    OfferingRepository offeringRepository;
    @Mock
    TenantRepository tenantRepository;

    @InjectMocks
    OfferingService service;

    Tenant tenant;
    Offering offering;
    UUID tenantId;
    UUID offeringId;
    OfferingRequestDTO requestDTO;
    OfferingResponseDTO responseDTO;


    @BeforeEach
    void seUp() {
        offeringId = UUID.randomUUID();
        tenantId = UUID.randomUUID();
        tenant = new Tenant(tenantId, "Barbearia", "barber", LocalDateTime.now());

        requestDTO = new OfferingRequestDTO("Corte Simples", 30, new BigDecimal("35.00"));

        offering = new Offering("Corte Simples", 30, new BigDecimal("35.00"), tenant);
        offering.setId(offeringId);
    }

    @Test
    @DisplayName("Deve criar um servico vinculado aquela empresa(tenant)")
    void shouldCreateServiceWithTenant() {

        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(tenant));
        when(offeringRepository.existsByNameAndTenantId(offering.getName(), tenantId)).thenReturn(false);
        when(offeringRepository.save(any(Offering.class))).thenReturn(offering);

        OfferingResponseDTO response = service.createService(tenantId, requestDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Corte Simples", response.name());

        verify(tenantRepository, times(1)).findById(tenantId);
        verify(offeringRepository, times(1)).existsByNameAndTenantId(offering.getName(), tenantId);
        verify(offeringRepository, times(1)).save(any(Offering.class));
    }

    @Test
    @DisplayName("Deve lançar uma exception se não existir a empresa(tenant)")
    void shouldThrowNotFoundExceptionForTenantInCreate() {

        when(tenantRepository.findById(tenantId)).thenReturn(Optional.empty());

        NotFoundExceptionT ex = Assertions.assertThrows(NotFoundExceptionT.class, () -> {
            service.createService(tenantId,requestDTO);
        }, "DEVERIA LANÇAR UMA EXCEPTION");

        String message = "Tenant não existe, tente novamente.";

        Assertions.assertEquals(message, ex.getMessage());
        verify(tenantRepository, times(1)).findById(tenantId);
        verify(offeringRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve Lançar uma Exception de conflict se tiver o mesmo nome de servico dentro da empresa(tenant")
    void shouldThrowConflictExceptionWithDuplicateName() {

        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(tenant));
        when(offeringRepository.existsByNameAndTenantId(requestDTO.name(), tenantId)).thenReturn(true);

        ExceptionConflict ex = Assertions.assertThrows(ExceptionConflict.class, () -> {
            service.createService(tenantId, requestDTO);
        }, "DEVERIA LANÇAR UMA EXCEPTIONS");

        String message = "Nome de serviço já cadastrado!";

        Assertions.assertEquals(message, ex.getMessage());
        verify(tenantRepository, times(1)).findById(tenantId);
        verify(offeringRepository, times(1)).existsByNameAndTenantId(requestDTO.name(), tenantId);
        verify(offeringRepository, never()).save(any());

    }

}
