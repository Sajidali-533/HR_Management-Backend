package com.management.hr_managemnet.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.hr_managemnet.dto.AttandenceRegister;
import com.management.hr_managemnet.model.Attandence;
import com.management.hr_managemnet.model.Employee;
import com.management.hr_managemnet.model.User;
import com.management.hr_managemnet.repository.AttendenceRepository;
import com.management.hr_managemnet.repository.EmployeeRepository;
import com.management.hr_managemnet.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping ("/api/attandences")
@RequiredArgsConstructor
public class AttandenceController {
    @Autowired
    private AttendenceRepository attendenceRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired 
    private UserRepository userRepository;

    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<String> addAttandence(@RequestBody AttandenceRegister attandenceRegister){
        Optional<User> existingUser = userRepository.findByUsername(attandenceRegister.getUsername());
        if(!existingUser.isPresent()){
            return ResponseEntity.badRequest().body("User Not Found!");
        }
        Long userId = existingUser.get().getId();
        Optional<Employee> existingEmployee = employeeRepository.findByUserId(userId);

        Attandence attandence = new Attandence();
        attandence.setDate(attandenceRegister.getDate());
        attandence.setCheckIn(attandenceRegister.getCheckIn());
        attandence.setCheckOut(attandenceRegister.getCheckOut());
        attandence.setStatus(attandenceRegister.getStatus());
        attandence.setEmployee(existingEmployee.get());
        attendenceRepository.save(attandence);
        return ResponseEntity.ok("Added Attandence Successfully!");
        
    }
}
