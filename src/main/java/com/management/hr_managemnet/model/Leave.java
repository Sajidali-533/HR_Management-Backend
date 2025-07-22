package com.management.hr_managemnet.model;

import java.time.LocalDate;

import com.management.hr_managemnet.enums.LeaveStatus;

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
@Table (name = "leaves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leave {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate startData;
    private LocalDate endData;
    private String reason;
    private LocalDate appliedOn;

    @Enumerated (EnumType.STRING)
    private LeaveStatus status;

    @ManyToOne
    @JoinColumn (name = "employee_id")
    private Employee employee;
}
