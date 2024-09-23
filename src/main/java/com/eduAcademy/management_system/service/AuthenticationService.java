package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.AuthenticationRequeste;
import com.eduAcademy.management_system.dto.AuthenticationResponse;
import com.eduAcademy.management_system.dto.ChangePasswordRequest;
import com.eduAcademy.management_system.dto.RegisterRequeste;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.repository.UserRepository;
import com.eduAcademy.management_system.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

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

    private String generateActivationCode(int length) {
        String characters="0123456789";
        StringBuilder codeBuilder=new StringBuilder();
        SecureRandom random=new SecureRandom();
        for (int i=0 ; i<length ;i++){
            int randomIndex=random.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }


    public AuthenticationResponse authenticate(AuthenticationRequeste requeste) {
        if (userRepository.findByEmail(requeste.getEmail()).isEmpty()) {
            throw new IllegalArgumentException("L'email existe pas.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requeste.getEmail(),
                        requeste.getPassword())
        );

        var user=userRepository.findByEmail(requeste.getEmail()).orElseThrow();
        var jwtToken=jwtService.gerenateToken(user);
        System.out.println(jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
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
