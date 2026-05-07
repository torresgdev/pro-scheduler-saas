package com.example.appointment.repositories;

import com.example.appointment.models.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, UUID> {

@Query("""
        SELECT SUM(f.amount) FROM FinancialTransaction f
        WHERE f.tenant.id = :tenantId
        AND f.type = com.example.appointment.models.enums.TransactionType.ENTRY
        AND f.transactionDate BETWEEN :start AND :end
        """)
    BigDecimal calculateTotalRevenue(UUID tenantId, LocalDateTime start, LocalDateTime end);

    List<FinancialTransaction> findAllByTenantIdOrderByTransactionDateDesc(UUID tenantId);

}
