package com.eduAcademy.management_system.dto;

import com.eduAcademy.management_system.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffResponseDto {
    private String email;
    private boolean active;
    private String clubRef;
    private String ObjectId;
    private String phone;
    private String firstName;
    private String lastName;
    private List<String> roles;
}
