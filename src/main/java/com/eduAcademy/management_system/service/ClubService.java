package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.dto.ClubResponseDto;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.mapper.ClubMapper;
import com.eduAcademy.management_system.repository.ClubRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubService implements ClubServiceInterface {

    private final ClubRepository clubRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int REFERENCE_LENGTH = 10;
    private static final SecureRandom random = new SecureRandom();
    private final ClubMapper clubMapper;


    @Override
    @PreAuthorize("hasRole('SUPERADMIN')")
    @Transactional
    public ClubRequestDto createClub(ClubRequestDto clubRequestDto) throws MessagingException, IOException {
        Club club = clubMapper.ClubDTOToClub(clubRequestDto);

        if (clubRepository.findByEmail(clubRequestDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("The email " + clubRequestDto.getEmail() + " is already in use.");
        }

        List<String> roles = clubRequestDto.getRoles().stream()
                .map(roleName -> {
                    try {
                        com.eduAcademy.management_system.enums.Role roleEnum = com.eduAcademy.management_system.enums.Role.valueOf(roleName);
                        return roleEnum.name();
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Invalid role: " + roleName);
                    }
                })
                .collect(Collectors.toList());


        club.setRoles(roles);
        club.setFirstName(clubRequestDto.getFirstName());
        club.setLastName(clubRequestDto.getLastName());
        club.setClubAddress(clubRequestDto.getClubAddress());
        club.setCreated_at(LocalDateTime.now());
        club.setUpdated_at(LocalDateTime.now());
        club.setReference(generateUniqueReference());
        club.setEmail(clubRequestDto.getEmail());
        club.setActive(false);

        Club savedClub = clubRepository.save(club);
        return clubMapper.ClubToClubDTO(savedClub);
    }

    @Override
    public String generateUniqueReference() {
        String reference;
        do {
            reference = generateRandomReference();
        } while (clubRepository.existsByReference(reference));
        return reference;
    }

    @Override
    public Page<Club> getClubs(int size,int page) {
        Pageable pageable= PageRequest.of(page,size);
        return clubRepository.findAll(pageable);
    }

    @Override
    public ClubResponseDto updateClub(String clubRef,ClubRequestDto clubRequestDto) throws MessagingException {

        Club club = clubRepository.findByReference(clubRef)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));


        List<String> roles = clubRequestDto.getRoles().stream()
                .map(roleName -> {
                    try {
                        com.eduAcademy.management_system.enums.Role roleEnum = com.eduAcademy.management_system.enums.Role.valueOf(roleName);
                        return roleEnum.name();
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Invalid role: " + roleName);
                    }
                })
                .collect(Collectors.toList());


        club.setClubAddress(clubRequestDto.getClubAddress());
        club.setFirstName(clubRequestDto.getFirstName());
        club.setLastName(clubRequestDto.getLastName());
        club.setUpdated_at(LocalDateTime.now());
        club.setRoles(roles);

        clubRepository.save(club);

        return clubMapper.ClubToClubDtoResponse(club);
    }

    private String generateRandomReference() {
        StringBuilder reference = new StringBuilder(REFERENCE_LENGTH);
        for (int i = 0; i < REFERENCE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            reference.append(CHARACTERS.charAt(index));
        }
        return reference.toString();
    }

    public String uploadLogo(MultipartFile logo ,String clubRef) throws IOException {
        String uploadDir = "src/main/resources/static/assets/";
        Path uploadPath = Paths.get(uploadDir);

        Club club = clubRepository.findByReference(clubRef)
                .orElseThrow(() -> new IllegalArgumentException("clubRef not found"));

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = logo.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(logo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        club.setLogo(fileName);

        return fileName;
    }


}
