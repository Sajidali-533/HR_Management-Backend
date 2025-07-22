package com.management.hr_managemnet.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.management.hr_managemnet.enums.AttandenceStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "attandences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attandence {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime checkIn;
    private LocalTime checkOun;

    @Enumerated (EnumType.STRING)
    private AttandenceStatus status;
    
    @ManyToOne
    @JoinColumn (name = "employee_id")
    private Employee employee;
}
