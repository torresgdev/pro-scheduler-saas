package com.example.appointment;


import com.example.appointment.dtos.ProfessionalRequestDTO;
import com.example.appointment.dtos.ProfessionalResponseDTO;
import com.example.appointment.dtos.ProfessionalUpdateDTO;
import com.example.appointment.exceptions.NotFoundExceptionT;
import com.example.appointment.models.Professional;
import com.example.appointment.models.Tenant;
import com.example.appointment.repositories.ProfessionalRepository;
import com.example.appointment.repositories.TenantRepository;
import com.example.appointment.services.ProfessionalService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ProfessionalServiceTest {

    @Mock
    ProfessionalRepository professionalRepository;
    @Mock
    TenantRepository tenantRepository;

    @InjectMocks
    ProfessionalService service;

    private UUID tenantId;
    private UUID professionalId;
    private Tenant tenant;
    private Professional professional;
    private ProfessionalRequestDTO requestDto;
    private ProfessionalResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        professionalId = UUID.randomUUID();
        tenant = new Tenant(tenantId, "Barbearia do Cosme", "barber-cosme", LocalDateTime.now());
        professional = new Professional("Renato Abreu", "Profissional qualificado há 15 anos no mercado", tenant);
        professional.setId(professionalId);
        requestDto = new ProfessionalRequestDTO("Renato Abreu", "Profissional qualificado há 15 anos no mercado", tenantId);
        responseDTO = new ProfessionalResponseDTO(professionalId,"Renato Abreu", "Profissional qualificado há 15 anos no mercado");
    }


    @Test
    @DisplayName("Deve Criar um Profissional vinculado a sua Empresa(tenant)")
    void shouldCreateProfessionalBoundedByTenant() {

        when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(tenant));
        when(professionalRepository.save(any(Professional.class))).thenReturn(professional);

        ProfessionalResponseDTO result = service.createProfessional(tenantId,requestDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Renato Abreu",result.name());
        Assertions.assertEquals(professionalId, result.id());
        verify(tenantRepository, times(1)).findById(tenantId);
        verify(professionalRepository, times(1)).save(any(Professional.class));

    }

    @Test
    @DisplayName("Deve lançar uma exception de notfound para tenant nao encontrado")
    void shouldThrowNotFoundExceptionForTenant() {
        when(tenantRepository.findById(tenantId)).thenReturn(Optional.empty());

        NotFoundExceptionT ex = Assertions.assertThrows(NotFoundExceptionT.class, () -> {
            service.createProfessional(tenantId,requestDto);
        }, "DEVERIA SOLTAR UMA EXCEPTION");

        String message = "Tenant não encontrado para vincular o profissional";

        Assertions.assertNotNull(ex);
        Assertions.assertEquals(message, ex.getMessage());
        verify(professionalRepository, never()).save(any(Professional.class));
    }

    @Test
    @DisplayName("Deve Atualizar um profissional que esteja vinculado a empresa")
    void shouldUpdateProfessionalBoundedWithTenant() {
        ProfessionalUpdateDTO updateDTO = new ProfessionalUpdateDTO(
                Optional.of("Sandrin"),
                Optional.of("")
        );

        when(professionalRepository.findByIdAndTenantId(professionalId, tenantId)).thenReturn(Optional.of(professional));
        when(professionalRepository.save(any())).thenReturn(professional);

        ProfessionalResponseDTO result = service.updateProfessional(professionalId,tenantId, updateDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Sandrin", result.name());
        verify(professionalRepository, times(1)).findByIdAndTenantId(professionalId, tenantId);
        verify(professionalRepository, times(1)).save(any(Professional.class));
    }

    @Test
    @DisplayName("Deve Lançar uma Exception de NotFound se não existir Profissional com ID")
    void shouldThrowNotFoundExceptionForProfessional() {
        ProfessionalUpdateDTO updateDTO = new ProfessionalUpdateDTO(
                Optional.of("Sandrin"),
                Optional.of("")
        );
        when(professionalRepository.findByIdAndTenantId(professionalId, tenantId)).thenReturn(Optional.empty());

        NotFoundExceptionT ex = Assertions.assertThrows(NotFoundExceptionT.class, () -> {
            service.updateProfessional(professionalId, tenantId, updateDTO);
        }, "DEVERIA LANÇAR UMA EXCEPTION");

        String message = "ID não encontrado" +
                " tente novamente com ID válido.";


        Assertions.assertNotNull(ex);
        Assertions.assertEquals(message, ex.getMessage());
        verify(professionalRepository, times(1)).findByIdAndTenantId(professionalId, tenantId);
        verify(professionalRepository, never()).save(any(Professional.class));
    }

}
