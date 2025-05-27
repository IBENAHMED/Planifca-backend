package com.eduAcademy.management_system.dto;

import com.eduAcademy.management_system.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDto {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private List<String> roles;

}
