package com.example.appointment.services;

import com.example.appointment.dtos.ScheduleBlockRequestDTO;
import com.example.appointment.dtos.ScheduleBlockResponseDTO;
import com.example.appointment.exceptions.ExceptionConflict;
import com.example.appointment.exceptions.NotFoundExceptionT;
import com.example.appointment.models.Professional;
import com.example.appointment.models.ScheduleBlock;
import com.example.appointment.models.Tenant;
import com.example.appointment.repositories.AppointmentRepository;
import com.example.appointment.repositories.ProfessionalRepository;
import com.example.appointment.repositories.ScheduleBlockRepository;
import com.example.appointment.repositories.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleBlockService {
    private final ScheduleBlockRepository scheduleBlockRepository;
    private final ProfessionalRepository professionalRepository;
    private final AppointmentRepository appointmentRepository;
    private final TenantRepository tenantRepository;


    public ScheduleBlockResponseDTO createBlock(UUID tenantId, UUID professionalId, ScheduleBlockRequestDTO requestDTO) {
        Professional profe = professionalRepository.findByTenantIdAndId(tenantId, professionalId).orElseThrow(
                () -> new NotFoundExceptionT("Profissional não encontrado")
        );

        boolean hasClient = appointmentRepository.hasConflictAppointment(professionalId, requestDTO.startTime(), requestDTO.endTime());
        if (hasClient) {
            throw new ExceptionConflict("Não é possivel bloquear esse horario pois já existe cliente agendado");
        }

        boolean hasBlock = scheduleBlockRepository.existsConflict(professionalId, requestDTO.startTime(), requestDTO.endTime());
        if (hasBlock) {
            throw new ExceptionConflict("Ja existe um bloqueio programado para esse período");
        }

        ScheduleBlock scheduleBlock = new ScheduleBlock(
                profe,
                requestDTO.startTime(),
                requestDTO.endTime(),
                requestDTO.reason()
        );
        ScheduleBlock saved = scheduleBlockRepository.save(scheduleBlock);

        return ScheduleBlockResponseDTO.fromModel(saved);


    }

    public List<ScheduleBlockResponseDTO> findAllByProfessional(UUID tenantId, UUID professionalId) {

        Professional professional = professionalRepository.findByTenantIdAndId(tenantId,professionalId).orElseThrow(
                () -> new NotFoundExceptionT("Profissional não encontrado")
        );

        return  scheduleBlockRepository.findAllByProfessionalId(professional.getId())
                .stream()
                .map(ScheduleBlockResponseDTO::fromModel)
                .toList();

    }

    public void deleteBlock(UUID tenantId, UUID professionalId, UUID blockId) {
        professionalRepository.findByTenantIdAndId(tenantId,professionalId).orElseThrow(
                () -> new NotFoundExceptionT("Profissional não encontrado")
        );

        ScheduleBlock block = scheduleBlockRepository.findById(blockId).orElseThrow(() ->  new NotFoundExceptionT("Bloqueio não encontrado"));

        if (!block.getProfessional().getId().equals(professionalId)) {
            throw new ExceptionConflict("Este bloqueio não pertence ao profissional informado");
        }

        scheduleBlockRepository.delete(block);
    }
}
