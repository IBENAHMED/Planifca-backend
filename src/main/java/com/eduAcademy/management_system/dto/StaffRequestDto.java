package com.eduAcademy.management_system.dto;


import com.eduAcademy.management_system.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffRequestDto {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private boolean active;
    private List<String> roles;
}
