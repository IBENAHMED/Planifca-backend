package com.eduAcademy.management_system.entity;

import com.eduAcademy.management_system.enums.TypeSport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Stadium {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String terrainId;
    private TypeSport typeSport;
    private int pricePerHour;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

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
