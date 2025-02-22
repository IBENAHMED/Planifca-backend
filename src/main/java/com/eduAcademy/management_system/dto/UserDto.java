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
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private boolean active;
    private Club club;
    private Set<String> roles;
}
