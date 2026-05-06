package com.example.appointment.repositories;

import com.example.appointment.models.Appointment;
import com.example.appointment.models.ScheduleBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ScheduleBlockRepository extends JpaRepository<ScheduleBlock, UUID> {

    @Query("""
            SELECT COUNT(s) > 0 FROM ScheduleBlock s
            WHERE s.professional.id = :professionalId
            AND :start < s.endTime
            AND :end > s.starTime
            """)
    boolean existsConflict(
            @Param("professionalId") UUID professionalId,
            @Param("start")LocalDateTime start,
            @Param("end") LocalDateTime end
            );

    List<ScheduleBlock> findAllByProfessionalId(UUID professionalId);
    List<ScheduleBlock> findAllByProfessionalIDAndStartTimeBetween(
            UUID professionalId,
            LocalDateTime start,
            LocalDateTime end
    );
}
