package com.example.appointment.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "professionals")
@SQLDelete(sql = "UPDATE professionals SET active = false WHERE id = ?") // Substitui o DELETE físico
@SQLRestriction("active = true")
public class Professional extends BaseEntity{

    @Column(nullable = false, length = 100)
    private String name;

    private String bio;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "work_start_time")
    private LocalTime workStartTime;

    @Column(name = "work_end_time")
    private LocalTime workEndTime;

    public Professional(String name, String bio, Tenant tenant) {
        this.name = name;
        this.bio = bio;
        this.setTenant(tenant);
    }
}
