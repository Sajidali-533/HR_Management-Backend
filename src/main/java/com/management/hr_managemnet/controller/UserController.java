package com.management.hr_managemnet.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.hr_managemnet.dto.RegisterRequest;
import com.management.hr_managemnet.dto.UserResponseDto;
import com.management.hr_managemnet.enums.Role;
import com.management.hr_managemnet.model.User;
import com.management.hr_managemnet.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exist");
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User newUser = User.builder()
                            .username(registerRequest.getUsername())
                            .password(encodedPassword)
                            .role(registerRequest.getRole())
                            .build();
        User savedUser = userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDto.fromUser(savedUser));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUser(){
        List<User> users = userRepository.findAll();
        List<UserResponseDto> userDto = users.stream()
                                            .map(UserResponseDto::fromUser)
                                            .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/role/{roleName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<UserResponseDto>> getUserByRole(@PathVariable String roleName){
        try{
            Role role = Role.valueOf(roleName.toUpperCase());
            List<User> users = userRepository.findByRole(role);

            List<UserResponseDto> userDto = users.stream()
                                                .map(UserResponseDto::fromUser)
                                                .collect(Collectors.toList());
            return ResponseEntity.ok(userDto);
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(java.util.Collections.emptyList());
        }
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR') or authentication.principal.username == #username")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username){
        return userRepository.findByUsername(username)
                            .map(UserResponseDto::fromUser)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }
}


