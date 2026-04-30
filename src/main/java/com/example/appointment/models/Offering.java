package com.example.appointment.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "services")
@SQLDelete(sql = "UPDATE services SET active = false WHERE id = ?") // Substitui o DELETE físico
@SQLRestriction("active = true")
public class Offering extends BaseEntity{
    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    public Offering(String name, Integer durationMinutes, BigDecimal price, Tenant tenant) {
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.setTenant(tenant);
    }
}
