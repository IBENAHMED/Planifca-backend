package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.Role;
import com.eduAcademy.management_system.mapper.ClubMapper;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.repository.RoleRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService implements ClubServiceInterface {

    private final ClubRepository clubRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int REFERENCE_LENGTH = 10;
    private static final SecureRandom random = new SecureRandom();
    private final RoleRepository roleRepository;
    private final ClubMapper clubMapper;
    private final PasswordEncoder passwordEncoder;
    private final GenerateActivationToken activationTokenService;

    @Override
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ClubRequestDto createClub(ClubRequestDto clubRequestDto) throws MessagingException {
        Club club = clubMapper.ClubDTOToClub(clubRequestDto);

        if (clubRepository.findByEmail(clubRequestDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("The email " + clubRequestDto.getEmail() + " is already in use.");
        }

        Set<Role> roles = clubRequestDto.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName)))
                .collect(Collectors.toSet());

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
    @Transactional
    public void activateAccount(String password, String confirmPassword, String token) {
        String email = activationTokenService.extractEmailFromActivationToken(token);

        boolean isValid = activationTokenService.validateActivationToken(token);
        if (!isValid) {
            throw new IllegalArgumentException("Invalid or expired activation token");
        }

        Club club = clubRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (club.isActive()) {
            throw new IllegalStateException("Account is already activated");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        club.setActive(true);
        club.setPassword(passwordEncoder.encode(password));
        club.setConfirmPassword(passwordEncoder.encode(confirmPassword));
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

    private String generateRandomReference() {
        StringBuilder reference = new StringBuilder(REFERENCE_LENGTH);
        for (int i = 0; i < REFERENCE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            reference.append(CHARACTERS.charAt(index));
        }
        return reference.toString();
    }
}
