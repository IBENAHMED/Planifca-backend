package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.dto.AuthenticationRequestDto;
import com.eduAcademy.management_system.dto.AuthenticationResponseDto;
import com.eduAcademy.management_system.dto.RegisterRequestDto;
import com.eduAcademy.management_system.service.serviceImpl.AuthServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@RestController
@RequestMapping("/api/internal/auth")
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    public AuthController(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }

    @PostMapping(path = "register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto request,@RequestHeader String clubRef) {
        try {
            authServiceImpl.register(request, clubRef);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error has occurred.");
        }
    }

    @PostMapping(path = "login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto authenticate ,@RequestHeader String clubRef) {
        try {
            AuthenticationResponseDto response = authServiceImpl.authenticate(authenticate,clubRef);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new AuthenticationResponseDto(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthenticationResponseDto("An internal error has occurred."));
        }
    }
}

