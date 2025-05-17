package com.eduAcademy.management_system.service.serviceImpl;

import com.eduAcademy.management_system.dto.ActivationRequestDto;
import com.eduAcademy.management_system.dto.PasswordChangeRequest;
import com.eduAcademy.management_system.entity.PasswordResetToken;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.exception.BadRequestException;
import com.eduAcademy.management_system.exception.ConflictException;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.repository.PasswordRestTokenRepository;
import com.eduAcademy.management_system.repository.UserRepository;
import com.eduAcademy.management_system.service.UserPasswordService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements UserPasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordRestTokenRepository restTokenRepository;
    private final EmailService emailService;


    @Override
    public void changeUserPassword(PasswordChangeRequest passwordChangeRequest) throws BadRequestException {
        User user = userRepository.findByEmail(passwordChangeRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + passwordChangeRequest.getEmail()));

        if (!passwordEncoder.matches(passwordChangeRequest.getOldPassword(), user.getPassword())) {
            throw new ConflictException("Old password incorrect");
        }

        if (passwordEncoder.matches(passwordChangeRequest.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("The new password cannot be the same as the old one.");
        }

        if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
            throw new BadRequestException("The confirmation password must be the same as the new password.");
        }

        String hashedNewPassword = passwordEncoder.encode(passwordChangeRequest.getNewPassword());
        user.setPassword(hashedNewPassword);
        userRepository.save(user);
    }

    private String generateSecureToken(int byteLength) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[byteLength];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @Override
    public void forgotPassword(String email, String clubRef) throws MessagingException, IOException {
        User user = userRepository.findByEmailAndClubReference(email, clubRef)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        restTokenRepository.findByUser(user).ifPresent(restTokenRepository::delete);

        String token = generateSecureToken(64);

        PasswordResetToken newToken = PasswordResetToken.builder()
                .token(token)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .user(user)
                .build();

        restTokenRepository.save(newToken);

        String resetUrl = "http://localhost:4200/reset-password?token=" + token;

        emailService.sendPasswordResetEmail(email, resetUrl);
    }

    @Override
    public void resetPassword(PasswordChangeRequest passwordChangeRequest) {
        PasswordResetToken resetToken = restTokenRepository.findByToken(passwordChangeRequest.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token has expired");
        }

        User user = resetToken.getUser();

        if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
            throw new BadRequestException("The confirmation password must be the same as the new password.");
        }

        user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        userRepository.save(user);

        restTokenRepository.delete(resetToken);
    }

    public void activateAccount(ActivationRequestDto requestDto, String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Invalid activation link"));

        if (user.isActive()) {
            throw new ConflictException("Your account is already activated.");
        }

        if (!requestDto.getNewPassword().equals(requestDto.getConfirmPassword())) {
            throw new ConflictException("The confirmation password must be the same as the new password.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getNewPassword());
        user.setPassword(encodedPassword);

        user.setActive(true);
        userRepository.save(user);
    }

}
