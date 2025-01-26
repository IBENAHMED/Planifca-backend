package com.eduAcademy.management_system.controller;


import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.repository.ClubConfigRepository;
import com.eduAcademy.management_system.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eduAcademy.management_system.entity.ClubConfiguration;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/clubConfig")

public class ClubConfigurationController {
    private final ClubRepository clubRepository;
    private final ClubConfigRepository configRepository;

    @GetMapping("{entityRef}")
    public ResponseEntity<?> getClubConfiguration(@PathVariable String entityRef) {
        Optional<Club> club = clubRepository.findByReference(entityRef);

        // Vérifier si le club existe
        if (club.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Club not found for reference: " + entityRef);
        }

        // Récupérer la configuration du club
        System.out.println("#atyq "+club.get().getId());
        ClubConfiguration configuration = configRepository.findById(club.get().getId()).get();

        // Vérifier si la configuration existe


        // Retourner les addons du club
        return ResponseEntity.ok(configuration.getAddons());
    }

}
