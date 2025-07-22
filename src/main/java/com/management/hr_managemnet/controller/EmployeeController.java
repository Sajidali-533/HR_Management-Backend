package com.management.hr_managemnet.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.hr_managemnet.dto.RegisterEmployee;
import com.management.hr_managemnet.model.Employee;
import com.management.hr_managemnet.model.User;
import com.management.hr_managemnet.repository.EmployeeRepository;
import com.management.hr_managemnet.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    @PreAuthorize ("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<String> registerEmployee(@RequestBody RegisterEmployee registerEmployee){
        Optional<User> existingUsers = userRepository.findByUsername(registerEmployee.getUsername());

        if (!existingUsers.isPresent()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Employee employee = new Employee();
        employee.setUser(existingUsers.get());
        employee.setName(registerEmployee.getName());
        employee.setEmail(registerEmployee.getEmail());
        employee.setPhone(registerEmployee.getPhone());
        employee.setDepartment(registerEmployee.getDepartment());
        employee.setDesignation(registerEmployee.getDesignation());
        employee.setDoj(registerEmployee.getDoj());
        employee.setSalary(registerEmployee.getSalary());

        employeeRepository.save(employee);
        return ResponseEntity.ok("Employee registered successfully");
    }

    @PutMapping("/updateEmployee")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<String> updateEmployee (@RequestBody RegisterEmployee registerEmployee){
        Optional<User> existingUser = userRepository.findByUsername(registerEmployee.getUsername());
        if(!existingUser.isPresent()){
            return ResponseEntity.badRequest().body("User not found");
        }
        Long userId = existingUser.get().getId();

        Optional<Employee> existingEmployee = employeeRepository.findByUserId(userId);

        if(existingEmployee.isPresent()){
            Employee employee = existingEmployee.get();
            employee.setName(registerEmployee.getName());
            employee.setEmail(registerEmployee.getEmail());
            employee.setPhone(registerEmployee.getPhone());
            employee.setDepartment(registerEmployee.getDepartment());
            employee.setDesignation(registerEmployee.getDesignation());
            employee.setDoj(registerEmployee.getDoj());
            employee.setSalary(registerEmployee.getSalary());
            employeeRepository.save(employee);
            return ResponseEntity.ok("Employee Updated Successfully!");
        }else{
            return ResponseEntity.badRequest().body("Update failed!");
        }
    }

    @GetMapping("/allEmployee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Employee>> getAllEmployee(){
        List<Employee> allEmployees = employeeRepository.findAll();
        return ResponseEntity.ok(allEmployees);
    }

    @GetMapping("/department/{dePartment}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<Employee>> getEmployeeByDepartment(@PathVariable String dePartment){
        String d_part = dePartment.toUpperCase();
        List<Employee> d_Employees = employeeRepository.findByDepartment(d_part);
        return ResponseEntity.ok(d_Employees);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN','HR') or authentication.principal.username == #username")
    public ResponseEntity<Optional<Employee>> getEmployeeByUsername(@PathVariable String username){
        Optional<User> existingUser = userRepository.findByUsername(username);

        if(!existingUser.isPresent()){
            return ResponseEntity.badRequest().body(Optional.empty());
        }
        Long userId = existingUser.get().getId();

        Optional<Employee> getEmployee = employeeRepository.findByUserId(userId);
        return ResponseEntity.ok(getEmployee);
    }
}
