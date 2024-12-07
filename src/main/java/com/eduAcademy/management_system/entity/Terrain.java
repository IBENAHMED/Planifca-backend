package com.eduAcademy.management_system.entity;

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
public class Terrain {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String terrainId;
    private String typeSport;
    private int pricePerHour;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;
    @OneToMany(mappedBy = "terrain")
    private List<Reservation> reservationList;

}
