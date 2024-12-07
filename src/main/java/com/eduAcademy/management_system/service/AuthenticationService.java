package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.AuthenticationRequeste;
import com.eduAcademy.management_system.dto.AuthenticationResponse;
import com.eduAcademy.management_system.dto.ChangePasswordRequest;
import com.eduAcademy.management_system.dto.RegisterRequeste;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.repository.UserRepository;
import com.eduAcademy.management_system.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ClubRepository clubRepository;

    public void register(RegisterRequeste requeste) {
        if (userRepository.findByEmail(requeste.getEmail()).isPresent()) {
            throw new IllegalArgumentException("L'email est déjà utilisé.");
        }
        var user= User.builder()
                .firstname(requeste.getFirstname())
                .lastname(requeste.getLastname())
                .email(requeste.getEmail())
                .password(passwordEncoder.encode(requeste.getPassword()))
                .active(true)
                .role(requeste.getRole())
                .build();
        userRepository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequeste requeste) {
        Optional<User> userOpt = userRepository.findByEmail(requeste.getEmail());
        Optional<Club> clubOpt = clubRepository.findByEmail(requeste.getEmail());

        if (userOpt.isEmpty() && clubOpt.isEmpty()) {
            throw new IllegalArgumentException("L'email n'existe pas.");
        }
        if (userOpt.isPresent()) {
            var user = userOpt.get();

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requeste.getEmail(),
                            requeste.getPassword()
                    )
            );
            var jwtToken = jwtService.gerenateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).build();
        }

        if (clubOpt.isPresent()) {
            var club = clubOpt.get();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requeste.getEmail(),
                            requeste.getPassword()
                    )
            );
            var jwtToken = jwtService.gerenateToken(club);

            return AuthenticationResponse.builder().token(jwtToken).build();
        }
        throw new IllegalArgumentException("Erreur d'authentification.");
    }

    public void  changePassword(ChangePasswordRequest request, Principal userConnect) {
        var user= (User)((UsernamePasswordAuthenticationToken) userConnect).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("votre mot de passe actuel n'est pas correct");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas : le nouveau mot de passe et la confirmation doivent être identiques.");
        }
        if (passwordEncoder.matches(request.getNewPassword(),user.getPassword())) {
            throw new IllegalArgumentException("Veuillez choisir un mot de passe qui diffère du mot de passe actuel.");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
