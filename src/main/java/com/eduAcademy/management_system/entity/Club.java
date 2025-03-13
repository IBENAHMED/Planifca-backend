package com.eduAcademy.management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Club {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String reference;
    @Column(unique = true,nullable = false)
    private String email;
    private boolean active;
    private String logo;
    @Column(unique = true, nullable = false)
    private String frontPath;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "club")
    private List<Stadium> stadiums;

//    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
//    private List<Reservation> reservations = new ArrayList<>();

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
