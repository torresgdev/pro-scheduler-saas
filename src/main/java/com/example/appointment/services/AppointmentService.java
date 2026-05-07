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
import com.example.appointment.repositories.ScheduleBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ProfessionalRepository professionalRepository;
    private final OfferingRepository offeringRepository;
    private final ScheduleBlockRepository scheduleBlockRepository;
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
        LocalDateTime start = requestDTO.startTime();

        Appointment appointment = new Appointment(client, prof, offering, start);

        LocalDateTime end = appointment.getEndTime();

        // valida se o horario esta dentro da jornada de trabalho
        validateWorkHours(prof, start, end);

        // valida se o profissional está realmente disponivel no momento
        scheduleBlock(prof, start, end);

        // validar conflito
        boolean hasConflict = appointmentRepository.hasConflictAppointment(prof.getId(), start, end);

        if (hasConflict) {
            throw new ExceptionConflict("O profissional já possui um agendamento para esse horario");
        }

        //salvar
        appointment.setTenant(prof.getTenant());
        appointment.setEndTime(end);

        appointment = appointmentRepository.save(appointment);
        return AppointmentResponseDTO.fromModel(appointment);
    }

    public List<AppointmentResponseDTO> listAllAppointments(UUID tenantId, UUID professionalId) {
        if (!professionalRepository.existsByTenantIdAndId(tenantId, professionalId)) {
            throw new NotFoundExceptionT("Profissional não encontrado no estabelecimento");
        }

        List<Appointment> list =  appointmentRepository.findAllByTenantIdAndProfessionalId(tenantId, professionalId);
        return list.stream().map(AppointmentResponseDTO::fromModel).toList();
    }

    public List<AppointmentResponseDTO> listAllAppointmentsByDay(UUID tenantId, UUID professionalId, LocalDate date) {
        if (!professionalRepository.existsByTenantIdAndId(tenantId,professionalId)) {
            throw new NotFoundExceptionT("Profissional não encontrado");
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        System.out.println("Buscando entre: " + startOfDay + " e " + endOfDay);

        List<Appointment> list = appointmentRepository.findAllByProfessionalAndDateRange(professionalId,startOfDay,endOfDay);
        return list.stream().map(AppointmentResponseDTO::fromModel).toList();


    }


    private void validateWorkHours(Professional prof, LocalDateTime start, LocalDateTime end) {
        LocalTime appointmentStart = start.toLocalTime();
        LocalTime appointmentEnd = end.toLocalTime();

        // inicio não pode ser antes do horario de abertura, o fim nao pode ser depois do horario de fechamento
        if (appointmentStart.isBefore(prof.getWorkStartTime()) ||
        appointmentEnd.isAfter(prof.getWorkEndTime())) {

            throw new ExceptionConflict("O profissional não trabalha neste horário");
        }
    }

    private void scheduleBlock(Professional prof, LocalDateTime blockStartTime, LocalDateTime blockEndTime) {
        boolean hasBlock = scheduleBlockRepository.existsConflict(prof.getId(), blockStartTime, blockEndTime);
        if (hasBlock) {
            throw new ExceptionConflict("O profissional está em horario de pausa nesse momento");
        }
    }
}
