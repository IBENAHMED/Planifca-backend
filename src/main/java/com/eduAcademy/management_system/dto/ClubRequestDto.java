package com.eduAcademy.management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubRequestDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String reference;
    private String email;
    private String clubAddress;
    private boolean active;
    private String logo;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private List<String> roles;
}
