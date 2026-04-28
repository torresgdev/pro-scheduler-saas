package com.example.appointment.dtos;

import com.example.appointment.models.Tenant;

import java.time.LocalDateTime;
import java.util.UUID;

public record TenantResponseDTO(
        UUID id,
        String name,
        String slug,
        LocalDateTime createdAt
) {
    public static TenantResponseDTO fromEntity(Tenant tenant) {
        return new TenantResponseDTO(
                tenant.getId(),
                tenant.getName(),
                tenant.getSlug(),
                tenant.getCreatedAt()
        );
    }
}
