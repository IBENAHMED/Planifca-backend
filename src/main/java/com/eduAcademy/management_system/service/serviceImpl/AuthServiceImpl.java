package com.eduAcademy.management_system.service.serviceImpl;

import com.eduAcademy.management_system.dto.*;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.Roles;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.enums.RoleName;
import com.eduAcademy.management_system.exception.ConflictException;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.exception.UnauthorizedException;
import com.eduAcademy.management_system.mapper.UserMapper;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.repository.UserRepository;
import com.eduAcademy.management_system.security.JwtService;
import com.eduAcademy.management_system.service.AuthService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request, String clubRef) {
        try {
            User user = userRepository.findByEmailAndClubReference(request.getEmail(), clubRef)
                    .orElseThrow(() -> new NotFoundException("User not found with email : " + request.getEmail()));

            if (!user.isActive()) {
                throw new UnauthorizedException("Your account is not activated. Please check your email.");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String jwtToken = jwtService.generateTokenForUser(user);

            return AuthenticationResponseDto.builder()
                    .token(jwtToken)
                    .build();

        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Incorrect credentials");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred", e);
        }
    }
}