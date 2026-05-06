package com.example.appointment.repositories;

import com.example.appointment.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query("""
        SELECT COUNT(a) > 0 FROM Appointment a 
        WHERE a.professional.id = :profId 
        AND a.status != 'CANCELLED'
        AND (:start < a.endTime AND :end > a.startTime)
    """)
    boolean hasConflictAppointment(
            @Param("profId") UUID profId,
            @Param("start")LocalDateTime start,
            @Param("end") LocalDateTime end
            );

    List<Appointment> findAllByProfessionalIDAndStartTimeBetween(
            UUID professionalId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Appointment> findAllByTenantIdAndProfessionalId(UUID tenantId, UUID professionalId);


}
