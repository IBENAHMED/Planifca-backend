package com.eduAcademy.management_system.dto;

import com.eduAcademy.management_system.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private boolean active;
    private Set<String> roles;
}
