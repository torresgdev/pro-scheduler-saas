package com.example.appointment;

import com.example.appointment.dtos.AppointmentRequestDTO;
import com.example.appointment.models.Offering;
import com.example.appointment.models.Professional;
import com.example.appointment.models.Tenant;
import com.example.appointment.repositories.OfferingRepository;
import com.example.appointment.repositories.ProfessionalRepository;
import com.example.appointment.repositories.TenantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders; // Importação manual
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext; // Importante

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AppointmentControllerIT {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private OfferingRepository offeringRepository;

    private UUID tenantId;
    private AppointmentRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // CONFIGURAÇÃO MANUAL DO MOCKMVC
        // Isso substitui o @AutoConfigureMockMvc e resolve o problema do import
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Limpar banco para garantir isolamento (opcional com @Transactional)
        tenantRepository.deleteAll();

        // Criar o tenant
        Tenant tenant = new Tenant(null, "Barbearia Teste", "barber-teste", LocalDateTime.now());
        tenant = tenantRepository.save(tenant);
        this.tenantId = tenant.getId();

        // Criar profissional
        Professional professional = new Professional("Marcos", "Expert", tenant);
        professional.setWorkStartTime(LocalTime.of(8,0));
        professional.setWorkEndTime(LocalTime.of(18,0));
        professional = professionalRepository.save(professional);

        // Criar serviço
        Offering service = new Offering("Corte", 30, new BigDecimal("50"), tenant);
        service = offeringRepository.save(service);

        // Preparar o DTO
        requestDTO = new AppointmentRequestDTO(
                professional.getId(),
                service.getId(),
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
                "Cliente Teste",
                "11999999999"
        );
    }

    @Test
    @DisplayName("Deve criar um agendamento via API e retornar 201")
    void shouldCreateAppointmentViaApi() throws Exception {
        mockMvc.perform(post("/tenants/{tenantId}/appointments", tenantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientName").value("Cliente Teste"));
    }
}