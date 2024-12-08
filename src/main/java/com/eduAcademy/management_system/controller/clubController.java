package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.dto.ActivationAccountRequestDto;
import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.dto.ClubResponseDto;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.mapper.ClubMapper;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.service.ClubService;
import com.eduAcademy.management_system.service.ConfigurationClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/club")
public class clubController {

    private final ClubService clubService;
    private final ConfigurationClientService configurationClientService;
    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;

    @PostMapping("newClub")
    @Transactional
    public ResponseEntity<?> createClub(@RequestBody ClubRequestDto request) {
        try {
            ClubRequestDto clubRequestDto =clubService.createClub(request);
            Club club=clubMapper.ClubDTOToClub(clubRequestDto);
            ClubResponseDto clubResponseDto =clubMapper.ClubToClubDtoResponse(club);
            configurationClientService.createClientConfigFile(clubRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(clubResponseDto);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while creating the club configuration file : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error while creating the club : " + e.getMessage());
        }
    }

    @GetMapping("/{ref}")
    public ResponseEntity<ClubResponseDto> getClubByRef(@PathVariable String ref) {
        Optional<Club> clubOptional = clubRepository.findByReference(ref);

        if (clubOptional.isPresent()) {
            ClubResponseDto clubResponseDto = clubMapper.ClubToClubDtoResponse(clubOptional.get());
            return ResponseEntity.ok(clubResponseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestBody ActivationAccountRequestDto request) {
        try {

            clubService.activateAccount(request.getPassword(),request.getConfirmPassword(),request.getToken());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Account activated");
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error. Please try again later.");
        }
    }

    @GetMapping("allClubs")
    public ResponseEntity<Page<Club>> getClubs(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Club> clubs = clubService.getClubs(page, size);
        return ResponseEntity.ok(clubs);
    }


}
