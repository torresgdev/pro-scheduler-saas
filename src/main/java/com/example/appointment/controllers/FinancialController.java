package com.example.appointment.controllers;

import com.example.appointment.dtos.FinancialTransactionResponseDTO;
import com.example.appointment.services.FinancialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
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

    @GetMapping("/today-history")
    public ResponseEntity<List<FinancialTransactionResponseDTO>> getTodayHistory(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(financialService.getHistoryByToday(tenantId));
    }
}