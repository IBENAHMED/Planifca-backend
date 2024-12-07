package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.ClubDTO;
import com.eduAcademy.management_system.dto.ConfirmPassword;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.ConfirmationToken;
import com.eduAcademy.management_system.entity.Role;
import com.eduAcademy.management_system.mapper.ClubMapper;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.repository.ConfirmationTokenRepository;
import com.eduAcademy.management_system.repository.RoleRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
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
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ClubDTO createClub(ClubDTO clubDTO) throws MessagingException {
        Club club = clubMapper.ClubDTOToClub(clubDTO);

        if (clubRepository.findByEmail(clubDTO.getEmail()).isPresent()){
            throw new IllegalArgumentException("The email " + clubDTO.getEmail() + " is already in use.");
        }

        Set<Role> roles = clubDTO.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName)))
                .collect(Collectors.toSet());

        club.setRoles(roles);
        club.setFirstName(clubDTO.getFirstName());
        club.setLastName(clubDTO.getLastName());
        club.setClubAddress(clubDTO.getClubAddress());
        club.setCreated_at(LocalDateTime.now());
        club.setUpdated_at(LocalDateTime.now());
        club.setReference(generateUniqueReference());
        club.setEmail(clubDTO.getEmail());
        club.setActive(false);

        Club savedClub = clubRepository.save(club);
        return clubMapper.ClubToClubDTO(savedClub);
    }

    @Override
    @Transactional
    public void activateAccount(ConfirmPassword request) {
        Optional<ConfirmationToken> tokenOptional = confirmationTokenRepository.findByToken(request.getToken());

        if (tokenOptional.isEmpty()) {
            throw new IllegalArgumentException("Token invalide.");
        }

        ConfirmationToken confirmationToken = tokenOptional.get();

        if (confirmationToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expiré.");
        }
        Club club = confirmationToken.getClub();
        if(club.isActive()){
            throw new IllegalArgumentException("The club is already active.");
        }
        club.setActive(true);
        club.setPassword(passwordEncoder.encode(request.getPassword()));
        clubRepository.save(club);
        confirmationTokenRepository.delete(confirmationToken);
    }

    @Override
    public String generateUniqueReference() {
        String reference;
        do {
            reference = generateRandomReference();
        } while (clubRepository.existsByReference(reference));
        return reference;
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
