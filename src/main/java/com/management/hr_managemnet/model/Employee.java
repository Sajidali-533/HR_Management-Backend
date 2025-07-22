package com.management.hr_managemnet.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn (name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private String name;
    private String email;
    private String phone;
    private String department;
    private String designation;
    private LocalDate doj;
    private BigDecimal salary;

    @OneToMany (mappedBy = "employee", cascade = CascadeType.ALL)
    List<Attandence> attandences;

    @OneToMany (mappedBy = "employee", cascade = CascadeType.ALL)
    List<Leave> leaves;

    @OneToMany (mappedBy = "employee", cascade = CascadeType.ALL)
    List<Payroll> payrolls;


}
