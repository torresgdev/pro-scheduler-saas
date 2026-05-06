package com.example.appointment.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ScheduleBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String reason; // Ex: "Almoço", "Folga", "Médico"

    @ManyToOne
    private Professional professional;

    public ScheduleBlock(Professional professional, LocalDateTime startTime, LocalDateTime endTime, String reason) {
        this.professional = professional;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
    }
}
