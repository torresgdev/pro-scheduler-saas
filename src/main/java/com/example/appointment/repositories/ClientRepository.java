package com.example.appointment.repositories;

import com.example.appointment.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    boolean existsByPhone(String phone);
}
