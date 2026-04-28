package com.example.appointment;


import com.example.appointment.dtos.TenantRequestDTO;
import com.example.appointment.dtos.TenantResponseDTO;
import com.example.appointment.dtos.TenantUpdateDTO;
import com.example.appointment.exceptions.ExceptionConflict;
import com.example.appointment.exceptions.NotFoundExceptionT;
import com.example.appointment.models.Tenant;
import com.example.appointment.repositories.TenantRepository;
import com.example.appointment.services.TenantService;
import org.junit.jupiter.api.Assertions;
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
public class TenantServiceTest {

    @Mock
    TenantRepository repository;

    @InjectMocks
    TenantService service;



    @Test
    @DisplayName("Deve Criar um Tenant com Sucesso")
    void shouldCreateTenantWithSuccess() {
        //ARRANGE
        TenantRequestDTO tenant = new TenantRequestDTO("BarbeariaMaximus", "barber-shop-max");
        when(repository.findBySlug(tenant.slug())).thenReturn(Optional.empty());
        //ACT
        TenantResponseDTO result = service.createTenant(tenant);

        //Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("barber-shop-max", result.slug());
        verify(repository, times(1)).save(any(Tenant.class));
    }

    @Test
    @DisplayName("Deve Lançar uma  ConlifctException se slug tiver ja cadastrado")
    void shouldThrowExceptionConflictForSlug() {
        //ARRANGE
        TenantRequestDTO tenant = new TenantRequestDTO("BarbeariaMaximus", "barber-shop-max");
        Tenant existTenant = new Tenant("JaExiste", "barber-shop-max");
        when(repository.findBySlug(tenant.slug())).thenReturn(Optional.of(existTenant));

        //ACT && Assert
        ExceptionConflict ex = Assertions.assertThrows(ExceptionConflict.class, () -> {
            service.createTenant(tenant);
        },"Deveria lançar um ExceptionConflict aqui");

        String message = "Slug já cadastrado";


        //Assert
        Assertions.assertNotNull(tenant);
        Assertions.assertEquals(message, ex.getMessage());
        verify(repository, never()).save(any(Tenant.class));
    }

    @Test
    @DisplayName("Deve Achar um Tenant pelo Id fornecido")
    void shouldFindTenantById() {
        //ARRANGE
        UUID id = UUID.fromString("7a8204a2-286d-44ad-b49c-8eb74a786fc2");
        Tenant tenant = new Tenant(id,"barber", "barber-shop", LocalDateTime.now());
        when(repository.findById(id)).thenReturn(Optional.of(tenant));

        //ACT
        TenantResponseDTO response = service.findById(id);

        //ASSERTIONS
        Assertions.assertNotNull(response);
        Assertions.assertEquals(id, response.id());
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve Lançar uma ExceptionNotFound se nao encontrar o ID fornecido")
    void shouldThrowExceptionNotFound() {
        //ARRANGE
        UUID id = UUID.fromString("7a8204a2-286d-44ad-b49c-8eb74a786fc2");
        when(repository.findById(id)).thenReturn(Optional.empty());

        //ACT && Assertions
        NotFoundExceptionT ex = Assertions.assertThrows(NotFoundExceptionT.class, () -> {
            service.findById(id);
        }, "Deveria lançar uma NOTFOUND EXCEPTION");

        String message = "Não encontrado, Revise os dados inseridos";

        //ASSERTIONS
        Assertions.assertNotNull(ex);
        Assertions.assertEquals(message, ex.getMessage());
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve Listar todos os Tenants cadastrados")
    void shouldListAllTenants() {
        //ARRANGE
        UUID id1 = UUID.fromString("7a8204a2-286d-44ad-b49c-8eb74a786fc2");
        Tenant tenant1 = new Tenant(id1,"barber", "barber-shop", LocalDateTime.now());

        UUID id2 = UUID.fromString("7a8204a2-286d-44ad-b49c-8eb74a786fd4");
        Tenant tenant2 = new Tenant(id2,"nails", "nails-colors", LocalDateTime.now());

       when(repository.findAll()).thenReturn(List.of(tenant1,tenant2));

       //ACT
       List<TenantResponseDTO> list = service.findAll();

       //ASSERTIONS
        Assertions.assertNotNull(list);
        Assertions.assertEquals("barber", list.getFirst().name());
        Assertions.assertEquals("nails-colors", list.getLast().slug());
        verify(repository, times(1)).findAll();

    }

    @Test
    @DisplayName("Deve lançar uma exception de notFOUND")
    void shouldThrowNotFoundExceptionForEmptyList() {
        //ARRANGE
        List<Tenant> tenant = new ArrayList<>();
        when(repository.findAll()).thenReturn(tenant);

        //ACT && ASSERTIONS
        NotFoundExceptionT ex = Assertions.assertThrows(NotFoundExceptionT.class, () -> {
            service.findAll();
        }, "DEVERIA LANçAR UMA EXCEPTION AQUI");

        String message = "Nennhum tenant encontrado nessa lista";

        Assertions.assertNotNull(ex);
        Assertions.assertEquals(message, ex.getMessage());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve dar Update no Tenant de acordo com Nome ou slug fornecido")
    void shouldUpdateTenantWithNameOrSlug() {
        //ARRANGE
        UUID id1 = UUID.fromString("7a8204a2-286d-44ad-b49c-8eb74a786fc2");
        Tenant tenant1 = new Tenant(id1,"barber", "barber-shop", LocalDateTime.now());


        TenantUpdateDTO updateDTO = new TenantUpdateDTO(
                Optional.of("Barbearia"),
                Optional.of("barber-max")
        );
        when(repository.findById(tenant1.getId())).thenReturn(Optional.of(tenant1));
        when(repository.existsBySlug("barber-max")).thenReturn(false);

        //ACT
        TenantResponseDTO response = service.updateTenant(tenant1.getId(), updateDTO);

        //Assertions
        Assertions.assertNotNull(response);
        Assertions.assertEquals("barber-max", response.slug());
        verify(repository, times(1)).save(any(Tenant.class));

    }

    @Test
    @DisplayName("Deve Lançar ConflictException para Slug existente")
    void shouldThrowExceptionConflictForExistsSlug() {
        //ARRANGE
        UUID id1 = UUID.fromString("7a8204a2-286d-44ad-b49c-8eb74a786fc2");
        Tenant tenant1 = new Tenant(id1,"barber", "barber-shop", LocalDateTime.now());

        TenantUpdateDTO updateDTO = new TenantUpdateDTO(
                Optional.of("Barbearia"),
                Optional.of("novo-slug")
        );
        when(repository.findById(tenant1.getId())).thenReturn(Optional.of(tenant1));
        when(repository.existsBySlug("novo-slug")).thenReturn(true);

        //ACT && ASSERTS
        ExceptionConflict ex = Assertions.assertThrows(ExceptionConflict.class, () -> {
            service.updateTenant(tenant1.getId(), updateDTO);
        }, "DEVERIA LANÇAR UMA EXCEPTION");

        String message = "Slug já cadastrado";

        Assertions.assertNotNull(ex);
        Assertions.assertEquals(message, ex.getMessage());
        verify(repository, times(1)).findById(tenant1.getId());
        verify(repository, times(1)).existsBySlug("novo-slug");



    }






    @Test
    @DisplayName("Deve Deletar um Tenant com o id fornecido")
    void shouldDeleteTenantById() {
        //ARRANGE
        UUID id1 = UUID.fromString("7a8204a2-286d-44ad-b49c-8eb74a786fc2");
        Tenant tenant1 = new Tenant(id1,"barber", "barber-shop", LocalDateTime.now());
        when(repository.findById(tenant1.getId())).thenReturn(Optional.of(tenant1));


        //ACT
        service.deleteTenant(tenant1.getId());

        verify(repository, times(1)).delete(tenant1);
    }





}
