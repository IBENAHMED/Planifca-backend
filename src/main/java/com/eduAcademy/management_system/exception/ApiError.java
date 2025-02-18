package com.eduAcademy.management_system.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private int status;
    private String message;
    private String path;
    private String error;
    private LocalDateTime timestamp;

    public ApiError(int status, String error, String message, String path, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.path = path;
        this.timestamp = timestamp;
    }

}
