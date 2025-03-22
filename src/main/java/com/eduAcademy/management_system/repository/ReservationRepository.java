package com.eduAcademy.management_system.repository;

import com.eduAcademy.management_system.dto.ReservationResponseDto;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.Reservation;
import com.eduAcademy.management_system.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
     Optional<Reservation> findByStadiumTerrainIdAndReservationDateAndReservationStatus(String stadiumId, LocalDateTime reservationDate, ReservationStatus reservationStatus);
     Optional<Reservation> findByStadiumTerrainIdAndReservationDateAndStartTimeAndEndTimeAndReservationStatusNot(String stadium_terrainId, LocalDateTime reservationDate, LocalTime startTime, LocalTime endTime, ReservationStatus reservationStatus);
     List<Reservation> findByStadiumTerrainIdAndReservationDate(String stadiumTerrainId, LocalDateTime reservationDate);
     Optional<Reservation> findByReservationId(String reservationId);
     Page<Reservation> findByClub(Club club, Pageable pageable);
     List<Reservation> findByReservationStatus(ReservationStatus reservationStatus);

     @Query("SELECT r.reservationStatus, COUNT(r) FROM Reservation r WHERE r.club.reference = :clubRef GROUP BY r.reservationStatus")
     List<Object[]> countReservationByReservationStatusAndClubReference(@Param("clubRef") String clubRef);

}
