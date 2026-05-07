package com.example.appointment.controllers;

import com.example.appointment.dtos.FinancialTransactionResponseDTO;
import com.example.appointment.services.FinancialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tenants/{tenantId}/financial")
@RequiredArgsConstructor
public class FinancialController {

    private final FinancialService financialService;

    @GetMapping("/history")
    public ResponseEntity<List<FinancialTransactionResponseDTO>> getHistory(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(financialService.getHistory(tenantId));
    }

    @GetMapping("/today-revenue")
    public ResponseEntity<BigDecimal> getTodayRevenue(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(financialService.getDailyReavenue(tenantId));
    }
}