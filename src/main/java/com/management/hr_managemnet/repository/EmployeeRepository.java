package com.management.hr_managemnet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.hr_managemnet.model.Employee;

public interface EmployeeRepository extends JpaRepository <Employee, Long> {
    Optional <Employee> findByUserId(Long userId);
    List<Employee> findByDepartment(String deparment);
}
