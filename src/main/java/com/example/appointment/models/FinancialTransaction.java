package com.example.appointment.models;


import com.example.appointment.models.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_transactions")
@Getter @Setter
public class FinancialTransaction extends BaseEntity{

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    public FinancialTransaction(BigDecimal amount, LocalDateTime transactionDate, TransactionType type, String description, Appointment appointment, Tenant tenant) {
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.type = type;
        this.description = description;
        this.appointment = appointment;
        this.tenant = tenant;
    }
}
