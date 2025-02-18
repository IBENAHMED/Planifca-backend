package com.eduAcademy.management_system.service.serviceImpl;

import com.eduAcademy.management_system.dto.AuthenticationRequestDto;
import com.eduAcademy.management_system.dto.AuthenticationResponseDto;
import com.eduAcademy.management_system.dto.RegisterRequestDto;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.enums.RoleName;
import com.eduAcademy.management_system.exception.ConflictException;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.exception.UnauthorizedException;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.repository.UserRepository;
import com.eduAcademy.management_system.security.JwtService;
import com.eduAcademy.management_system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
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

    public void register(RegisterRequestDto request, String clubRef) {
        Club club = clubRepository.findByReference(clubRef)
                .orElseThrow(() -> new NotFoundException("Club with reference <" + clubRef + "> not found"));

        if (userRepository.findByEmail(request.getEmail().trim()).isPresent()) {
            throw new ConflictException("The email <" + request.getEmail() + "> is already in use");
        }

        List<RoleName> roleNames = request.getRoles().stream()
                .map(role -> {
                    String cleanedRole = role.replaceAll("[^a-zA-Z]", "").toUpperCase();
                    try {
                        return RoleName.valueOf(cleanedRole);
                    } catch (IllegalArgumentException e) {
                        throw new NotFoundException("Invalid role: <" + role+">");
                    }
                })
                .collect(Collectors.toList());

        User user = User.builder()
                .firstName(request.getFirstname().trim())
                .lastName(request.getLastname().trim())
                .email(request.getEmail().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .userId(generateUserId())
                .roles(roleNames)
                .club(club)
                .build();

        userRepository.save(user);
    }


    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request,String clubRef) {
        try {
            User user = userRepository.findByEmailAndClubReference(request.getEmail(),clubRef)
                    .orElseThrow(() -> new NotFoundException("User not found with email : " + request.getEmail()));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String jwtToken = jwtService.generateTokenForUser(user);

            return AuthenticationResponseDto.builder()
                    .token(jwtToken)
                    .build();
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Incorrect credentials");
        } catch (NotFoundException e) {
            throw new NotFoundException("User not found");
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