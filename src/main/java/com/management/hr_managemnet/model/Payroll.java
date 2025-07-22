package com.management.hr_managemnet.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
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
@Table (name = "payrolls")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String month;
    private BigDecimal baseSalary;
    private BigDecimal deduction;
    private BigDecimal bonus;
    private BigDecimal netSalary;
    private String payslipUrl;

    @ManyToOne
    @JoinColumn (name = "employee_id")
    private Employee employee;
}
