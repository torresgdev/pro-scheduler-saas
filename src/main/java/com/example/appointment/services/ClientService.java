package com.example.appointment.services;

import com.example.appointment.dtos.ClientRequestDTO;
import com.example.appointment.dtos.ClientResponseDTO;
import com.example.appointment.exceptions.NotFoundExceptionT;
import com.example.appointment.models.Client;
import com.example.appointment.models.Tenant;
import com.example.appointment.repositories.ClientRepository;
import com.example.appointment.repositories.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final TenantRepository tenantRepository;

    public ClientResponseDTO getOrCreateClient(ClientRequestDTO requestDTO) {
        Client client = clientRepository.findByPhone(requestDTO.phone())
                .orElseGet(() -> {
                    Client newClient = new Client(requestDTO.name(), requestDTO.phone());
                    return clientRepository.save(newClient);
                });
        return ClientResponseDTO.fromModel(client);
    }

    public Client getOrCreateClientEntity(UUID tenantId, ClientRequestDTO dto) {
        return clientRepository.findByPhone(dto.phone())
                .orElseGet(() -> {
                    Client newClient = new Client();
                    newClient.setName(dto.name());
                    newClient.setPhone(dto.phone());

                    // BUSCAR O TENANT E SETAR NO CLIENTE
                    Tenant tenant = tenantRepository.findById(tenantId)
                            .orElseThrow(() -> new NotFoundExceptionT("Tenant não encontrado"));
                    newClient.setTenant(tenant);

                    return clientRepository.save(newClient);
                });
    }


}
