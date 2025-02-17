package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.dto.ClubResponseDto;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.exception.UnauthorizedException;
import com.eduAcademy.management_system.mapper.ClubMapper;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.service.serviceImpl.ClubServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/club")
public class ClubController {

    private final ClubServiceImpl clubService;
    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;

    @PostMapping("newClub")
    public ResponseEntity<?> createClub(@RequestBody ClubRequestDto requestDto){
        try {
            return ResponseEntity.ok(clubService.createClub(requestDto));

        } catch (UnauthorizedException e){
            throw new UnauthorizedException("Access denied");
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating club: " + e.getMessage());
        }
    }

    @GetMapping("/logo/{reference}")
    public ResponseEntity<Resource> getClubLogoByReference(@PathVariable String reference) {
        Optional<Club> clubOptional = clubRepository.findByReference(reference);

        if (clubOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Club club = clubOptional.get();
        String logoPath = "/static" + club.getLogo();

        Resource resource = new ClassPathResource(logoPath);
        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/png");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + club.getLogo() + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @GetMapping("check-front-path/{frontPath}")
    public ResponseEntity<ClubResponseDto> getClubByFrontPath(@PathVariable String frontPath){
        Optional<Club> club=clubRepository.findByFrontPath(frontPath);
        if (club.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ClubResponseDto responseDto=clubMapper.toClubResponseDto(club.get());
        return ResponseEntity.ok(responseDto);
    }

}
