package com.eduAcademy.management_system.service.serviceImpl;

import com.eduAcademy.management_system.dto.AuthenticationRequestDto;
import com.eduAcademy.management_system.dto.AuthenticationResponseDto;
import com.eduAcademy.management_system.dto.RegisterRequestDto;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.enums.RoleName;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.repository.UserRepository;
import com.eduAcademy.management_system.security.JwtService;
import com.eduAcademy.management_system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequestDto request,String clubRef) {
        Club club = clubRepository.findByReference(clubRef)
                .orElseThrow(() -> new IllegalArgumentException("Club with reference " + clubRef + " not found"));

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("The email is already in use");
        }


        List<RoleName> roleNames = request.getRoles().stream()
                .map(role -> {
                    try {
                        return RoleName.valueOf(role.replaceAll("[^a-zA-Z\\s]", "").toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Invalid role: " + role);
                    }
                })
                .collect(Collectors.toList());


        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .userId(generateUserId())
                .roles(roleNames)
                .club(club)
                .build();
        userRepository.save(user);
    }


    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + request.getEmail()));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String jwtToken = jwtService.generateTokenForUser(user);

            return AuthenticationResponseDto.builder()
                    .token(jwtToken)
                    .build();
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect credentials", e);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred", e);
        }
    }

    private String generateUserId() {
        Random random = new Random();

        int segment1 = random.nextInt(90) + 10;
        int segment2 = random.nextInt(90) + 10;
        int segment3 = random.nextInt(90) + 10;
        int segment4 = random.nextInt(90) + 10;

        return String.format("%02d:%02d:%02d:%02d", segment1, segment2, segment3, segment4);
    }
}