package com.eduAcademy.management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
}
