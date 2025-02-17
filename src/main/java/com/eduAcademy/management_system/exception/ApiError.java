package com.eduAcademy.management_system.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;

    public ApiError(int value, String message, String description) {
    }
}
