package com.eduAcademy.management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String clubAddress;
    private boolean active;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private List<String> roles;
}
