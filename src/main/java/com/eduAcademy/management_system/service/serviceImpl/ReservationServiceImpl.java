package com.eduAcademy.management_system.service.serviceImpl;

import com.eduAcademy.management_system.dto.CancelReservationDto;
import com.eduAcademy.management_system.dto.ReservationDto;
import com.eduAcademy.management_system.dto.ReservationResponseDto;
import com.eduAcademy.management_system.dto.ReservationUpdateDto;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.Reservation;
import com.eduAcademy.management_system.entity.Stadium;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.enums.ReservationStatus;
import com.eduAcademy.management_system.exception.ConflictException;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.mapper.ReservationMapper;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.repository.ReservationRepository;
import com.eduAcademy.management_system.repository.StadiumRepository;
import com.eduAcademy.management_system.repository.UserRepository;
import com.eduAcademy.management_system.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClubRepository clubRepository;
    private final StadiumRepository stadiumRepository;
    private final ReservationMapper reservationMapper;
    private final UserRepository userRepository;

    @Override
    public String generateReservationId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(12);

        for (int i = 0; i < 12; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString().toUpperCase();
    }

    @Override
    @Transactional
    public ReservationResponseDto createReservation(ReservationDto reservationDto,String clubRef,String terrainId) {
        Club club = clubRepository.findByReference(clubRef)
                .orElseThrow(() -> new NotFoundException("Club not found with path <'" + clubRef + "'>"));

        Stadium stadium=stadiumRepository.findByTerrainId(terrainId)
                .orElseThrow(() -> new NotFoundException("stadium not found with id <'" + terrainId + "'>"));

        LocalDateTime reservationDateTime = reservationDto.getReservationDate();
        LocalTime startTime = reservationDateTime.toLocalTime();

        validateReservationTime(reservationDateTime, startTime, reservationDto.getEndTime());


        boolean isAvailable = isSlotAvailable(terrainId, reservationDateTime,
                startTime, reservationDto.getEndTime(),"");

        if (!isAvailable) {
            throw new ConflictException("The stadium is not available for this period!");
        }


       Reservation reservation=reservationMapper.toReservation(reservationDto);
       reservation.setClub(club);
       reservation.setStadium(stadium);
       reservation.setReservationDate(reservationDateTime);
       reservation.setStartTime(startTime);
       reservation.setEndTime(reservationDto.getEndTime());
       reservation.setReservationId(generateReservationId());
       reservation.setClientFirstName(reservationDto.getClientFirstName());
       reservation.setClientLastName(reservationDto.getClientLastName());
       reservation.setClientPhoneNumber(reservationDto.getClientPhoneNumber());
       reservation.setReservationStatus(ReservationStatus.PROGRAMMEE);

       reservationRepository.save(reservation);

        return reservationMapper.toReservationResponseDto(reservation);
    }

    @Override
    public boolean isStadiumAvailable(String terrainId, LocalDateTime date, LocalTime startTime, LocalTime endTime) {
        List<Reservation> existingReservations = reservationRepository.findByStadiumTerrainIdAndReservationDate(terrainId, date);

        for (Reservation reservation : existingReservations) {
            if (startTime.isBefore(reservation.getEndTime()) && endTime.isAfter(reservation.getStartTime())) {
                return false;
            }
        }

        return true;
    }

    private boolean isSlotAvailable(String terrainId, LocalDateTime reservationDate, LocalTime startTime, LocalTime endTime,String reservationIdToIgnore) {
        boolean isAvailable = isStadiumAvailable(terrainId, reservationDate, startTime, endTime);


        if (!isAvailable) {
            Optional<Reservation> cancelledReservation = reservationRepository.findByStadiumTerrainIdAndReservationDateAndReservationStatus(
                    terrainId, reservationDate, ReservationStatus.CANCELLED);

            if (cancelledReservation.isPresent()) {
                Optional<Reservation> activeReservation = reservationRepository.findByStadiumTerrainIdAndReservationDateAndStartTimeAndEndTimeAndReservationStatusNot(
                        terrainId, reservationDate,startTime,endTime, ReservationStatus.CANCELLED);



                if (!activeReservation.isPresent()) {
                    isAvailable = true;
                }
            }
        }

        return isAvailable;
    }

    private void validateReservationTime(LocalDateTime reservationDateTime, LocalTime startTime, LocalTime endTime) {
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new ConflictException("The reservation date and time cannot be in the past.");
        }

        LocalTime clubOpeningTime = LocalTime.of(6, 0);
        LocalTime clubClosingTime = LocalTime.of(1, 59);

        //if (startTime.isBefore(clubOpeningTime) || endTime.isAfter(clubClosingTime)) {
        //    throw new ConflictException("The reservation hours must be between 6:00 AM and 11:00 PM.");
        //}

        if (startTime.isAfter(endTime)) {
            throw new ConflictException("The start time cannot be after the end time.");
        }

        if (startTime.equals(endTime)) {
            throw new ConflictException("The start time cannot be equal to the end time.");
        }
    }



    @Override
    public void cancelReservation(String reservationId, CancelReservationDto cancelReservationDto) {
        Reservation reservation = reservationRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found with ID: " + reservationId));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> user=userRepository.findByEmail(currentUsername);

        if (reservation.getReservationStatus()==ReservationStatus.PROGRAMMEE) {
            reservation.setCancelBy(user.get().getEmail());
            reservation.setCancelReason(cancelReservationDto.getCancelReason());
            reservation.setReservationStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);
        } else {
            throw new ConflictException("cannot cancel this reservation: " + reservationId + " with status " + reservation.getReservationStatus());
        }

    }

    @Override
    public ReservationResponseDto updateReservation(String reservationId, ReservationUpdateDto updateDto) {
        Reservation existingReservation =reservationRepository.findByReservationId(reservationId)
                .orElseThrow(()-> new NotFoundException("Reservation not found with ID: " + reservationId));

        LocalDateTime reservationDateTime = updateDto.getReservationDate();
        LocalTime startTime = reservationDateTime.toLocalTime();

        validateReservationTime(reservationDateTime, startTime, updateDto.getEndTime());

        boolean isAvailable = isSlotAvailable(existingReservation.getStadium().getTerrainId(), reservationDateTime,
                startTime, updateDto.getEndTime(),reservationId);

        if (!isAvailable && !updateDto.getReservationDate().equals(existingReservation.getReservationDate())) {
            throw new ConflictException("The stadium is not available for this period!");
        }

        existingReservation.setReservationDate(reservationDateTime);
        existingReservation.setStartTime(startTime);
        existingReservation.setEndTime(updateDto.getEndTime());
        existingReservation.setClientPhoneNumber(updateDto.getClientPhoneNumber());
        existingReservation.setClientFirstName(updateDto.getClientFirstName());
        existingReservation.setClientLastName(updateDto.getClientLastName());

        reservationRepository.save(existingReservation);

        return reservationMapper.toReservationResponseDto(existingReservation);
    }

    @Override
    public ReservationResponseDto getReservation(String reservationId) {
        Reservation reservation=reservationRepository.findByReservationId(reservationId)
                .orElseThrow(()-> new NotFoundException("Reservation not found with ID: " + reservationId));

        return reservationMapper.toReservationResponseDto(reservation);
    }

    @Override
    public Page<ReservationResponseDto> getReservationsClub(int page, int size,String clubRef) {
        Club club = clubRepository.findByReference(clubRef)
                .orElseThrow(() -> new NotFoundException("Club with reference " + clubRef + " not found."));

        Pageable pageable = PageRequest.of(page, size);
        Page<Reservation> reservationsPage = reservationRepository.findByClub(club, pageable);
        return reservationsPage.map(reservationMapper::toReservationResponseDto);
    }

    @Override
    public void startReservation(String reservationId) {
        Reservation reservation=reservationRepository.findByReservationId(reservationId)
                .orElseThrow(()-> new NotFoundException("Reservation not found with ID: " + reservationId));

        if (reservation.getReservationDate().toLocalDate().isEqual(LocalDate.now())){
            reservation.setReservationStatus(ReservationStatus.INPROGRESS);
            reservationRepository.save(reservation);
        } else {
            throw new ConflictException("The reservation can only be started on the same day. "+ reservation.getReservationDate());
        }
    }


}
