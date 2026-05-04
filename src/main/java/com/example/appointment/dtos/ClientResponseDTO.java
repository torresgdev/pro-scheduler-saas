package com.example.appointment.dtos;

import com.example.appointment.models.Client;

public record ClientResponseDTO(
        String name,
        String phone
) {
    public static ClientResponseDTO fromModel(Client client) {
        return new ClientResponseDTO(
                client.getName(),
                client.getPhone()
        );
    }
}
