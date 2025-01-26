package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.Staff;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivationAccountService {

    private final GenerateActivationToken activationToken;
    private final ClubRepository clubRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    public void activateAccount(String password, String confirmPassword, String token, String entityType) {
        String email = activationToken.extractEmailFromActivationToken(token);

        boolean isValid = activationToken.validateActivationToken(token);
        if (!isValid) {
            throw new IllegalArgumentException("Invalid or expired activation token");
        }

        if (entityType.equalsIgnoreCase("club")) {
            Club club = clubRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("club not found"));

            if (club.isActive()) {
                throw new IllegalStateException("Account is already activated");
            }
            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Passwords do not match");
            }
            club.setActive(true);
            club.setPassword(passwordEncoder.encode(password));
            club.setConfirmPassword(passwordEncoder.encode(confirmPassword));
            clubRepository.save(club);
        } else if (entityType.equalsIgnoreCase("staff")) {
            Staff staff = staffRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("Staff not found"));

            if (staff.isActive()) {
                throw new IllegalStateException("Account is already activated");
            }
            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Passwords do not match");
            }
            staff.setActive(true);
            staff.setPassword(passwordEncoder.encode(password));
            staff.setConfirmPassword(passwordEncoder.encode(confirmPassword));
            staffRepository.save(staff);
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + entityType);
        }
    }

}
