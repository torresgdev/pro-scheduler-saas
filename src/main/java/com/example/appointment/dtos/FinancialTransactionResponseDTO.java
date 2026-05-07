package com.example.appointment.dtos;

import com.example.appointment.models.Appointment;
import com.example.appointment.models.FinancialTransaction;
import com.example.appointment.models.enums.PaymentMethod;
import com.example.appointment.models.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record FinancialTransactionResponseDTO(
        UUID id,
        BigDecimal amount,
        LocalDateTime transactionDate,
        TransactionType type,
        PaymentMethod method,
        String description,
        UUID appointmentId,
        UUID tenantId
) {
    public static FinancialTransactionResponseDTO fromModel(FinancialTransaction financialTransaction) {
        return new FinancialTransactionResponseDTO(
                financialTransaction.getId(),
                financialTransaction.getAmount(),
                financialTransaction.getTransactionDate(),
                financialTransaction.getType(),
                financialTransaction.getPaymentMethod(),
                financialTransaction.getDescription(),
                financialTransaction.getAppointment() != null ? financialTransaction.getAppointment().getId() : null,
                financialTransaction.getTenant().getId()
        );
    }
}
