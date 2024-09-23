package com.eduAcademy.management_system.dto;

import com.eduAcademy.management_system.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequeste {
    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private Role role;

}
