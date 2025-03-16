package com.eduAcademy.management_system.service.serviceImpl;

import com.eduAcademy.management_system.entity.Reservation;
import com.eduAcademy.management_system.enums.ReservationStatus;
import com.eduAcademy.management_system.repository.ReservationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class ReservationStatusScheduler {

    private final ReservationRepository reservationRepository;

    @PostConstruct
    public void startImmediateUpdate() {
        updateReservationWithStatusProgrammee();
    }

    @Scheduled(cron = "0 0 * * * ?")
     void updateReservationWithStatusProgrammee(){
        log.info("Updating reservation statuses...");
        List<Reservation> reservationListProgrammee=reservationRepository.findByReservationStatus(ReservationStatus.PROGRAMMEE);
        for (Reservation reservation:reservationListProgrammee) {

            LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), reservation.getEndTime());

            if (endDateTime.isBefore(LocalDateTime.now())) {
                reservation.setReservationStatus(ReservationStatus.EXPIRED);
                reservationRepository.save(reservation);
            }
        }
    }

    @Scheduled(cron = "0 * * ? * * ")
    void updateReservationWithStatusInprogress(){
        log.info("Updating reservation statuses...");
        List<Reservation> reservationListInprogress=reservationRepository.findByReservationStatus(ReservationStatus.INPROGRESS);

        for (Reservation reservation:reservationListInprogress) {

            LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), reservation.getEndTime());

            if (endDateTime.isBefore(LocalDateTime.now())) {
                reservation.setReservationStatus(ReservationStatus.FINISHED);
                reservationRepository.save(reservation);
            }
        }
    }
}
