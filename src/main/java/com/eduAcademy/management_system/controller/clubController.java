package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.dto.ClubDTO;
import com.eduAcademy.management_system.dto.ConfirmPassword;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.ConfirmationToken;
import com.eduAcademy.management_system.mapper.ClubMapper;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.service.ClubService;
import com.eduAcademy.management_system.service.ConfigurationClientService;
import com.eduAcademy.management_system.service.ConfirmationTokenService;
import com.eduAcademy.management_system.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/club")
public class clubController {

    private final ClubService clubService;
    private final ConfigurationClientService configurationClientService;
    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;

    @PostMapping("newClub")
    @Transactional
    public ResponseEntity<?> createClub(@RequestBody ClubDTO clubDTO) {
        try {
            ClubDTO club =clubService.createClub(clubDTO);
            configurationClientService.createClientConfigFile(club);
            return ResponseEntity.status(HttpStatus.CREATED).body(club);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while creating the club configuration file : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error while creating the club : " + e.getMessage());
        }
    }

    @GetMapping("/{ref}")
    public ResponseEntity<ClubDTO> getClubByRef(@PathVariable String ref) {
        Optional<Club> clubOptional = clubRepository.findByReference(ref);

        if (clubOptional.isPresent()) {
            ClubDTO clubDTO = clubMapper.ClubToClubDTO(clubOptional.get());
            return ResponseEntity.ok(clubDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestBody ConfirmPassword request) {
        try {
            clubService.activateAccount(request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Account activated");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du serveur.");
        }
    }


}
