package com.eduAcademy.management_system.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequest {
    private String token;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

}
