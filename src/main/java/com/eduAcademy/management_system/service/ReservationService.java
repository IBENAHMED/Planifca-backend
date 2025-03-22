package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;


public interface ReservationService {

    String generateReservationId();

    ReservationResponseDto createReservation(ReservationDto reservationDto,String clubRef,String terrainId);

    boolean isStadiumAvailable(String terrainId, LocalDateTime date, LocalTime startTime, LocalTime endTime);

    void cancelReservation(String reservationId, CancelReservationDto cancelReservationDto);

    ReservationResponseDto updateReservation(String reservationId, ReservationUpdateDto updateDto);

    ReservationResponseDto getReservation(String reservationId);

    Page<ReservationResponseDto> getReservationsClub(int page, int size,String clubRef);

    void startReservation(String reservationId);

    Map<String,Long> getReservationStatisticsByClub(String clubRef);



}
