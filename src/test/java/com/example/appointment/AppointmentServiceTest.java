package com.example.appointment;


import com.example.appointment.dtos.AppointmentRequestDTO;
import com.example.appointment.dtos.AppointmentResponseDTO;
import com.example.appointment.dtos.ClientRequestDTO;
import com.example.appointment.exceptions.ExceptionConflict;
import com.example.appointment.exceptions.NotFoundExceptionT;
import com.example.appointment.models.*;
import com.example.appointment.repositories.AppointmentRepository;
import com.example.appointment.repositories.OfferingRepository;
import com.example.appointment.repositories.ProfessionalRepository;
import com.example.appointment.services.AppointmentService;
import com.example.appointment.services.ClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    AppointmentRepository appointmentRepository;

    @Mock
    ProfessionalRepository professionalRepository;

    @Mock
    OfferingRepository offeringRepository;

    @Mock
    ClientService clientService;

    @InjectMocks
    AppointmentService appointmentService;


    Client client;
    Professional professional;
    Offering offering;
    Tenant tenant;
    AppointmentRequestDTO requestDTO;
    Appointment appointment;

    @BeforeEach
    void setUp() {
        UUID tenantId = UUID.randomUUID();
        UUID professionalId = UUID.randomUUID();
        UUID offeringId = UUID.randomUUID();
        UUID appointmentId = UUID.randomUUID();

        tenant = new Tenant(tenantId, "Barbearia", "barber-shop", LocalDateTime.now());
        client = new Client("Carlos", "2185464879");

        professional = new Professional("Jose", "profissional dedicado", tenant);
        professional.setId(professionalId);
        professional.setWorkStartTime(LocalTime.of(8,0));
        professional.setWorkEndTime(LocalTime.of(18,0));

        offering = new Offering("corte simples", 30, new BigDecimal("40"), tenant);
        offering.setId(offeringId);

        requestDTO = new AppointmentRequestDTO(professional.getId(), offering.getId(),LocalDateTime.now().plusDays(2), client.getName(), client.getPhone());
        appointment = new Appointment(client,professional,offering,LocalDateTime.now().plusDays(2));
        appointment.setId(appointmentId);
        appointment.setTenant(tenant);


    }

    @Test
    @DisplayName("Deve criar um appointment com sucesso")
    void shouldCreateAppointmentWithSuccess() {

        when(professionalRepository.findByTenantIdAndId(tenant.getId(),professional.getId())).thenReturn(Optional.of(professional));
        when(offeringRepository.findByTenantIdAndId(tenant.getId(), offering.getId())).thenReturn(Optional.of(offering));
        when(clientService.getOrCreateClientEntity(eq(tenant.getId()),any(ClientRequestDTO.class))).thenReturn(client);
        when(appointmentRepository.hasConflictAppointment(any(),any(),any())).thenReturn(false);

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        AppointmentResponseDTO result = appointmentService.createAppointment(tenant.getId(),requestDTO);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.id());
        Assertions.assertEquals("Carlos", result.clientName());

        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Deve lançar conflictexception com horarios")
    void shouldThrowExceptionwithConflictExceptionWithSameTimeAppointments() {
        LocalDateTime busyTime = LocalDateTime.now();
        AppointmentRequestDTO busyRequest = new AppointmentRequestDTO(professional.getId(),offering.getId(),busyTime, client.getName(), client.getPhone());
        when(professionalRepository.findByTenantIdAndId(tenant.getId(),professional.getId())).thenReturn(Optional.of(professional));
        when(offeringRepository.findByTenantIdAndId(tenant.getId(), offering.getId())).thenReturn(Optional.of(offering));
        when(clientService.getOrCreateClientEntity(eq(tenant.getId()),any(ClientRequestDTO.class))).thenReturn(client);
        when(appointmentRepository.hasConflictAppointment(any(),any(),any())).thenReturn(true);


        ExceptionConflict ex = Assertions.assertThrows(ExceptionConflict.class, () -> {
            appointmentService.createAppointment(tenant.getId(),busyRequest);
        }, "DEVERIA LANÇAR UMA EXCEPTION");

        String message = "O profissional já possui um agendamento para esse horario";

        Assertions.assertNotNull(ex);
        Assertions.assertEquals(message, ex.getMessage());
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Deve lançar exceptionConflict de que o profissional nao esta trabalhando nesse momento")
    void shouldThrowConflictExceptionForProfessionalNotWorkAtMoment() {
        AppointmentRequestDTO busyRequest = new AppointmentRequestDTO(professional.getId(),offering.getId(),LocalDateTime.now().plusHours(11), client.getName(), client.getPhone());

        when(professionalRepository.findByTenantIdAndId(tenant.getId(),professional.getId())).thenReturn(Optional.of(professional));
        when(offeringRepository.findByTenantIdAndId(tenant.getId(), offering.getId())).thenReturn(Optional.of(offering));
        when(clientService.getOrCreateClientEntity(eq(tenant.getId()),any(ClientRequestDTO.class))).thenReturn(client);

        ExceptionConflict ex = Assertions.assertThrows(ExceptionConflict.class, () -> {
            appointmentService.createAppointment(tenant.getId(),busyRequest);
        }, "DEVERIA LANÇAR UMA EXCEPTION");

        String message = "O profissional não trabalha neste horário";

        Assertions.assertNotNull(ex);
        Assertions.assertEquals(message, ex.getMessage());
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    @DisplayName("DEve lançar notFoundException para Profissional nao encontrado")
    void shouldThrowNotFoundExceptionForProfessional() {

        when(professionalRepository.findByTenantIdAndId(tenant.getId(),professional.getId())).thenReturn(Optional.empty());

        NotFoundExceptionT ex = Assertions.assertThrows(NotFoundExceptionT.class, () -> {
            appointmentService.createAppointment(tenant.getId(),requestDTO);
        }, "DEVERIA LANÇAR UMA EXEPTION");

        String message = "Profissional não encontrado";

        Assertions.assertNotNull(ex);
        Assertions.assertEquals(message, ex.getMessage());
        verify(professionalRepository, times(1)).findByTenantIdAndId(tenant.getId(),professional.getId());

    }


}
