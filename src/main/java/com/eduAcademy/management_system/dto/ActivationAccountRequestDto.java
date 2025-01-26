package com.eduAcademy.management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivationAccountRequestDto {
    private String password;
    private String confirmPassword;
    private String token;
    private String entityType;
}
