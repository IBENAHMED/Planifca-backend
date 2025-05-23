package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.dto.AuthenticationRequestDto;
import com.eduAcademy.management_system.dto.AuthenticationResponseDto;
import com.eduAcademy.management_system.dto.ChangePasswordRequest;
import com.eduAcademy.management_system.dto.RegisterRequestDto;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.service.AuthenticationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Log4j2
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "register")
    public ResponseEntity<User> register(@RequestBody RegisterRequestDto requeste){
        authenticationService.register(requeste);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(path = "login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody AuthenticationRequestDto authenticate) {
        AuthenticationResponseDto response = authenticationService.authenticate(authenticate);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "update-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal userConnect){
        authenticationService.changePassword(request,userConnect);
        return ResponseEntity.ok().build();
    }
}
