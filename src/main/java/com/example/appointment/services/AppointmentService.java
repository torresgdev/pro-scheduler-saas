package com.example.appointment.services;

import com.example.appointment.dtos.AppointmentRequestDTO;
import com.example.appointment.dtos.AppointmentResponseDTO;
import com.example.appointment.dtos.ClientRequestDTO;
import com.example.appointment.exceptions.ExceptionConflict;
import com.example.appointment.exceptions.NotFoundExceptionT;
import com.example.appointment.models.Appointment;
import com.example.appointment.models.Client;
import com.example.appointment.models.Offering;
import com.example.appointment.models.Professional;
import com.example.appointment.repositories.AppointmentRepository;
import com.example.appointment.repositories.OfferingRepository;
import com.example.appointment.repositories.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ProfessionalRepository professionalRepository;
    private final OfferingRepository offeringRepository;
    private final ClientService clientService;

    @Transactional
    public AppointmentResponseDTO createAppointment(UUID tenantId, AppointmentRequestDTO requestDTO) {

        // identificar ou criar cliente
        Client client = clientService.getOrCreateClientEntity(
                tenantId,
                new ClientRequestDTO(requestDTO.clientName(), requestDTO.clientPhone())
        );

        // buscar profissional e offering
        Professional prof = professionalRepository.findByTenantIdAndId(tenantId,requestDTO.professionalId()).orElseThrow(
                () ->  new NotFoundExceptionT("Profissional não encontrado")
        );
        Offering offering = offeringRepository.findByTenantIdAndId(tenantId, requestDTO.offeringId()).orElseThrow(
                () -> new NotFoundExceptionT("Serviço não encontrado")
        );

        // calcular horarios
        LocalDateTime start = requestDTO.starTime();
        LocalDateTime end = start.plusMinutes(offering.getDurationMinutes());

        // validar conflito
        boolean hasConflict = appointmentRepository.hasConflictAppointment(prof.getId(), start, end);

        if (hasConflict) {
            throw new ExceptionConflict("O profissional já possui um agendamento para esse horario");
        }

        //salvar
        Appointment appointment = new Appointment(client, prof, offering, start);
        appointment.setTenant(prof.getTenant());
        appointment.setEndTime(end);

        appointment = appointmentRepository.save(appointment);
        return AppointmentResponseDTO.fromModel(appointment);
    }
}
