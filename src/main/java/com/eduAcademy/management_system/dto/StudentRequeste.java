package com.eduAcademy.management_system.dto;

import com.eduAcademy.management_system.enums.Level;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentRequeste {

    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private String gender;
    private Level levelName;
}
