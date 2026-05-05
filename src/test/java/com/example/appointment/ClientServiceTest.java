package com.example.appointment;


import com.example.appointment.dtos.ClientRequestDTO;
import com.example.appointment.dtos.ClientResponseDTO;
import com.example.appointment.models.Client;
import com.example.appointment.repositories.ClientRepository;
import com.example.appointment.repositories.TenantRepository;
import com.example.appointment.services.ClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    ClientRepository clientRepository;

    @Mock
    TenantRepository tenantRepository;

    @InjectMocks
    ClientService clientService;



    @Test
    @DisplayName("Tenta  buscar um cliente com sucesso")
    void shouldCGetClient() {
        Client client = new Client("Carlos Almeida", "21980564879");
        ClientRequestDTO request = new ClientRequestDTO("Carlos Almeida", "21980564879");
        when(clientRepository.findByPhone(client.getPhone())).thenReturn(Optional.of(client));

        ClientResponseDTO result = clientService.getOrCreateClient(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Carlos Almeida", result.name());

        verify(clientRepository, times(1)).findByPhone(client.getPhone());
    }

    @Test
    @DisplayName("tenta criar cliente se  nao encontrar no banco o numero de telefone")
    void shouldCreateClientIfDontGetInfo() {
        Client client = new Client("Carlos Almeida", "21980564879");
        ClientRequestDTO request = new ClientRequestDTO("Carlos Almeida", "21980564879");

        when(clientRepository.findByPhone(client.getPhone())).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientResponseDTO result = clientService.getOrCreateClient(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Carlos Almeida", result.name());

        verify(clientRepository, times(1)).findByPhone(client.getPhone());
        verify(clientRepository, times(1)).save(any(Client.class));
    }


}
