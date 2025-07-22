package com.management.hr_managemnet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.hr_managemnet.enums.Role;
import com.management.hr_managemnet.model.User;

public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername (String username);
    List<User> findByRole(Role role);
}
