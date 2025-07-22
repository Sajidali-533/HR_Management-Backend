package com.management.hr_managemnet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.management.hr_managemnet.enums.Role;
import com.management.hr_managemnet.model.User;
import com.management.hr_managemnet.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInializer {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

   @PostConstruct
public void initAdminUser() {
    try {
        String defaultUsername = "admin";

        if (!userRepository.existsByUsername(defaultUsername)) {
            User admin = User.builder()
                    .username(defaultUsername)
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            System.out.println("✅ Default admin user created: username = admin, password = Admin@123");
        } else {
            System.out.println("ℹ️ Admin user already exists");
        }
    } catch (Exception e) {
        System.err.println("❌ Failed to initialize admin user:");
        e.printStackTrace();
    }
}

}
