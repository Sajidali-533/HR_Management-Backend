package com.management.hr_managemnet.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.hr_managemnet.model.Attandence;

public interface AttendenceRepository extends JpaRepository <Attandence, Long> {
    List<Attandence> findByEmployeeId(Long employeeId);
    List<Attandence> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate start, LocalDate end);
    Optional<Attandence> findByEmployeeIdAndDate(Long employeeId, LocalDate data);
    
}
