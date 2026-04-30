package com.example.appointment.repositories;

import com.example.appointment.models.Professional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfessionalRepository extends JpaRepository<Professional, UUID> {
    List<Professional> findAllByTenantId(UUID id);

    Optional<Professional> findByTenantIdAndId(UUID tenantId, UUID id);
}
