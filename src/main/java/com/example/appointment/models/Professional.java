package com.example.appointment.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "professionals")
public class Professional extends BaseEntity{

    @Column(nullable = false, length = 100)
    private String name;

    private String bio;

}
