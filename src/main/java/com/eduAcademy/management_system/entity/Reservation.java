package com.eduAcademy.management_system.entity;

import com.eduAcademy.management_system.enums.PaymentStatus;
import com.eduAcademy.management_system.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @Column(nullable = false, unique = true)
    private String reservationId;
    private LocalDateTime reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private ReservationStatus reservationStatus;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
//    @ManyToOne
//    @JoinColumn(name = "stadium_id")
//    private Stadium stadium;
//    @ManyToOne
//    @JoinColumn(name = "club_id", nullable = false)
//    private Club club;


    @PrePersist
    public void onPrePersist() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updated_at = LocalDateTime.now();
    }
}
