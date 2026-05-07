package com.example.appointment.repositories;

import com.example.appointment.models.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, UUID> {

}
