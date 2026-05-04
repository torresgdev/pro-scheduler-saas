package com.example.appointment.services;

import com.example.appointment.dtos.ClientRequestDTO;
import com.example.appointment.dtos.ClientResponseDTO;
import com.example.appointment.exceptions.ExceptionConflict;
import com.example.appointment.models.Client;
import com.example.appointment.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientResponseDTO createClient(ClientRequestDTO requestDTO) {
        Client client = clientRepository.findByPhone(requestDTO.phone())
                .orElseGet(() -> {
                    Client newClient = new Client(requestDTO.name(), requestDTO.phone());
                    return clientRepository.save(newClient);
                });
        return ClientResponseDTO.fromModel(client);
    }


}
