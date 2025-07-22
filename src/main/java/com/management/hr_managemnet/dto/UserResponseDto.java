package com.management.hr_managemnet.dto;

import com.management.hr_managemnet.enums.Role;
import com.management.hr_managemnet.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private Role role;

    public static UserResponseDto fromUser(User user){
        return new UserResponseDto(user.getId(), user.getUsername(), user.getRole());
    }
}
