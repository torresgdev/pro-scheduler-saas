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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

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
    @DisplayName("Deve buscar profissional todos profissionais da empresa(tenant)")
    void shouldFindAllProfessionalsBoundedWithTenant() {
        Professional professional1 = new Professional("Arrascaeta", "Esta machucado", tenant);
        professional1.setId(professionalId);
        List<Professional> list = new ArrayList<>(Arrays.asList(professional,professional1));



        when(tenantRepository.existsById(tenantId)).thenReturn(true);
        when(professionalRepository.findAllByTenantId(tenantId)).thenReturn(list);

        List<ProfessionalResponseDTO> result = service.findAllByTenant(tenantId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Renato Abreu", result.getFirst().name());
        Assertions.assertEquals("Arrascaeta", result.getLast().name());

        verify(tenantRepository, times(1)).existsById(tenantId);
        verify(professionalRepository, times(1)).findAllByTenantId(tenantId);
    }

    @Test
    @DisplayName("Deve lançar uma exception se o tenant nao existir ")
    void shouldThrowNotFoundExceptionForTenantInList() {

        when(tenantRepository.existsById(tenantId)).thenReturn(false);

        NotFoundExceptionT ex = Assertions.assertThrows(NotFoundExceptionT.class, () -> {
            service.findAllByTenant(tenantId);
        }, "DEVERIA LANÇAR UMA EXCEPTION");

        String message = "Empresa (tenant) não encontrada";

        Assertions.assertNotNull(ex);
        Assertions.assertEquals(message, ex.getMessage());

        verify(tenantRepository, times(1)).existsById(tenantId);
    }

    @Test
    @DisplayName("Deve Buscar apenas 1 profissional da empresa(tenant)")
    void shouldFindProfessionalBoundedWithTenant() {

        when(professionalRepository.findByTenantIdAndId(tenantId,professionalId)).thenReturn(Optional.of(professional));

        ProfessionalResponseDTO result = service.findByIdAndTenantId(tenantId,professionalId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Renato Abreu", professional.getName());
        Assertions.assertEquals("barber-cosme", tenant.getSlug());

        verify(professionalRepository, times(1)).findByTenantIdAndId(tenantId,professionalId);
    }

    @Test
    @DisplayName("Deve lançar exception se o profissional nao for encontrado na empresa")
    void shouldThrowExceptionNotFoundForProfessionalOnTenant() {
        when(professionalRepository.findByTenantIdAndId(tenantId,professionalId)).thenReturn(Optional.empty());

        NotFoundExceptionT ex = Assertions.assertThrows(NotFoundExceptionT.class, () -> {
            service.findByIdAndTenantId(tenantId,professionalId);
        }, "DEVERIA LANÇAR UMA EXCEPTION");

        String message = "Profissional não encontrado para essa empresa";

        Assertions.assertEquals(message, ex.getMessage());
        verify(professionalRepository, times(1)).findByTenantIdAndId(tenantId,professionalId);
    }


    @Test
    @DisplayName("Deve Atualizar um profissional que esteja vinculado a empresa")
    void shouldUpdateProfessionalBoundedWithTenant() {
        ProfessionalUpdateDTO updateDTO = new ProfessionalUpdateDTO(
                Optional.of("Sandrin"),
                Optional.of("")
        );

        when(professionalRepository.findByTenantIdAndId(professionalId, tenantId)).thenReturn(Optional.of(professional));
        when(professionalRepository.save(any())).thenReturn(professional);

        ProfessionalResponseDTO result = service.updateProfessional(professionalId,tenantId, updateDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Sandrin", result.name());
        verify(professionalRepository, times(1)).findByTenantIdAndId(professionalId, tenantId);
        verify(professionalRepository, times(1)).save(any(Professional.class));
    }

    @Test
    @DisplayName("Deve Lançar uma Exception de NotFound se não existir Profissional com ID")
    void shouldThrowNotFoundExceptionForProfessional() {
        ProfessionalUpdateDTO updateDTO = new ProfessionalUpdateDTO(
                Optional.of("Sandrin"),
                Optional.of("")
        );
        when(professionalRepository.findByTenantIdAndId(professionalId, tenantId)).thenReturn(Optional.empty());

        NotFoundExceptionT ex = Assertions.assertThrows(NotFoundExceptionT.class, () -> {
            service.updateProfessional(professionalId, tenantId, updateDTO);
        }, "DEVERIA LANÇAR UMA EXCEPTION");

        String message = "ID não encontrado" +
                " tente novamente com ID válido.";


        Assertions.assertNotNull(ex);
        Assertions.assertEquals(message, ex.getMessage());
        verify(professionalRepository, times(1)).findByTenantIdAndId(professionalId, tenantId);
        verify(professionalRepository, never()).save(any(Professional.class));
    }

    @Test
    @DisplayName("Deve deletar um professional com sucesso")
    void shouldDeleteProfessional() {

        when(professionalRepository.findById(professionalId)).thenReturn(Optional.of(professional));

        service.deleteProfessional(tenantId,professionalId);

        verify(professionalRepository, times(1)).findById(professionalId);
        verify(professionalRepository, times(1)).delete(professional);
    }

}
