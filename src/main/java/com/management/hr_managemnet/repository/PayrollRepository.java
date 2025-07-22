package com.management.hr_managemnet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.hr_managemnet.model.Payroll;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findByEmployeeId(Long employeeId);
    List<Payroll> findByEmployeeIdAndMonth(Long employeeId, String month);
}
