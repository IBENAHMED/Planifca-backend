package com.eduAcademy.management_system.entity;

import com.eduAcademy.management_system.enums.PaymentStatus;
import com.eduAcademy.management_system.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reservationId;
    private LocalDateTime reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private ReservationStatus reservationStatus;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private PaymentStatus paymentStatus;
    @ManyToOne
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Payment payment;
}
