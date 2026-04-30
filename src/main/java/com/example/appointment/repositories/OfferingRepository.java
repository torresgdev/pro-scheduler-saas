package com.example.appointment.repositories;


import com.example.appointment.models.Offering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OfferingRepository extends JpaRepository<Offering, UUID> {

    List<Offering> findAllByTenantId(UUID id);
    boolean existsByNameAndTenantId(String name, UUID tenantId);
}
