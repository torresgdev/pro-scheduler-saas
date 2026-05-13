package com.example.appointment.services;

import com.example.appointment.dtos.FinancialTransactionResponseDTO;
import com.example.appointment.models.FinancialTransaction;
import com.example.appointment.models.enums.AppointmentStatus;
import com.example.appointment.repositories.FinancialTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinancialService {
    private final FinancialTransactionRepository financialTransactionRepository;

    public List<FinancialTransactionResponseDTO> getHistory(UUID tenantId) {
        List<FinancialTransaction> list = financialTransactionRepository.findAllByTenantIdOrderByTransactionDateDesc(tenantId);
        return list.stream().map(FinancialTransactionResponseDTO::fromModel).toList();
    }

    public BigDecimal getDailyReavenue(UUID tenantId) {

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);

        BigDecimal total = financialTransactionRepository.calculateTotalRevenue(tenantId,start, end);
        return (total != null) ? total : BigDecimal.ZERO;
    }

    public List<FinancialTransactionResponseDTO> getHistoryByToday(UUID tenantId) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);

        return financialTransactionRepository.findDailyTransactions(tenantId, AppointmentStatus.COMPLETED,start,end).stream()
                .map(FinancialTransactionResponseDTO::fromModel).toList();

    }
}
