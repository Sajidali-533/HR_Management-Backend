package com.management.hr_managemnet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.hr_managemnet.enums.LeaveStatus;
import com.management.hr_managemnet.model.Leave;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByEmployeeId(Long employeeId);
    List<Leave> findByStatus(LeaveStatus status);
}
