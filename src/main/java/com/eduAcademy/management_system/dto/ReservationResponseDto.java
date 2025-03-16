package com.eduAcademy.management_system.dto;


import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.Stadium;
import com.eduAcademy.management_system.enums.ReservationStatus;
import com.eduAcademy.management_system.enums.TypeSport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime reservationDate;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private String clientFirstName;
    private String clientLastName;
    private String clientPhoneNumber;
    private String reservationId;
    private ReservationStatus reservationStatus;
    private String clubReference;
   private StadiumResponse stadium;

}
