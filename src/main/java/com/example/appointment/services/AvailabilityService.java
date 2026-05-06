package com.example.appointment.services;

import com.example.appointment.dtos.AvailabilitySlotDTO;
import com.example.appointment.exceptions.NotFoundExceptionT;
import com.example.appointment.models.Appointment;
import com.example.appointment.models.Professional;
import com.example.appointment.models.ScheduleBlock;
import com.example.appointment.repositories.AppointmentRepository;
import com.example.appointment.repositories.ProfessionalRepository;
import com.example.appointment.repositories.ScheduleBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvailabilityService {
    private ProfessionalRepository professionalRepository;
    private AppointmentRepository appointmentRepository;
    private ScheduleBlockRepository scheduleBlockRepository;



    public List<AvailabilitySlotDTO> getAvailability(UUID tenantId,UUID professionalId, LocalDate date){

        // busca profissional para saber o horario de trabalho
        Professional profe = professionalRepository.findById(professionalId).orElseThrow(() -> new
                NotFoundExceptionT("Profissional não encontrado"));

        // busca todos os agendamentos e bloqueios do dia
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentRepository.findAllByProfessionalIDAndStartTimeBetween(professionalId
        ,startOfDay,endOfDay);
        List<ScheduleBlock> blocks = scheduleBlockRepository.findAllByProfessionalIDAndStartTimeBetween(professionalId,
                startOfDay, endOfDay);


        // gerar os slots de 30 em 30 minutos
        List<AvailabilitySlotDTO> slots = new ArrayList<>();
        LocalTime current = profe.getWorkStartTime();

        while(current.isBefore(profe.getWorkEndTime())) {
            LocalDateTime currentDatetime = date.atTime(current);
            LocalTime next = current.plusMinutes(30);
            LocalDateTime nextDateTime = date.atTime(next);

            //verifica se conflito em agendamentos
            boolean hasAppointment = appointments.stream().anyMatch(a ->
                    currentDatetime.isBefore(a.getEndTime()) && nextDateTime.isAfter(a.getStartTime()));

            //verifica conflito em bloqueios
            boolean hasBlock = blocks.stream().anyMatch(b ->
                    currentDatetime.isBefore(b.getEndTime()) && nextDateTime.isAfter(b.getStartTime()));

            slots.add(new AvailabilitySlotDTO(current,
                    !hasAppointment && !hasBlock,
                    hasAppointment ? "Ocupado" : (hasBlock ? "Pausa" : "Livre")));
            current = next;
        }
        return slots;
    }


}
