package com.management.hr_managemnet.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.management.hr_managemnet.enums.AttandenceStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttandenceRegister {
    private String username;
    private Long employeeId;
    private LocalDate date;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private AttandenceStatus status;
}
