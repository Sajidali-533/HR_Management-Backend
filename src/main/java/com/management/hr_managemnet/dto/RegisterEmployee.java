package com.management.hr_managemnet.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEmployee {
    private String username;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String designation;
    private LocalDate doj;
    private BigDecimal salary;
}
