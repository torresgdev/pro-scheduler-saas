package com.example.appointment.dtos;


import java.util.Optional;

public record TenantUpdateDTO(
        Optional<String> name,
        Optional<String> slug
) {
}
