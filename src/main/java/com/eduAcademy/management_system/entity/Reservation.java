package com.eduAcademy.management_system.entity;

import com.eduAcademy.management_system.enums.PaymentStatus;
import com.eduAcademy.management_system.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime reservationDate;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    @ManyToOne
    @JoinColumn(name = "terrainId", referencedColumnName = "terrainId", nullable = false)
    private Stadium stadium;
    @ManyToOne
    @JoinColumn(name = "clubRef", referencedColumnName = "clubRef", nullable = false)
    private Club club;
    private String clientFirstName;
    private String clientLastName;
    private String clientPhoneNumber;
    private String cancelReason;
    private String cancelBy;


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
