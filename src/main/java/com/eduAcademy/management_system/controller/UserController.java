package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.dto.AuthenticationRequestDto;
import com.eduAcademy.management_system.dto.AuthenticationResponseDto;
import com.eduAcademy.management_system.dto.RegisterRequestDto;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.service.UserService;
import jakarta.mail.MessagingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/internal/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto request) {
        try {
            userService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error has occurred.");
        }
    }


    @PostMapping(path = "login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto authenticate) {
        try {
            AuthenticationResponseDto response = userService.authenticate(authenticate);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(
                    new AuthenticationResponseDto(e.getReason())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthenticationResponseDto("An internal error has occurred."));
        }
    }


    @PostMapping("forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        try {
            userService.sendPasswordResetEmail(email);
            return ResponseEntity.ok("The password reset email has been sent.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while sending the email.");
        }
    }

    @PostMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestHeader("Authorization") String tokenHeader,@RequestBody Map<String, String> request) {
        String token = tokenHeader.replace("Bearer ", "");
        String newPassword = request.get("newPassword");

        boolean success = userService.resetPassword(token, newPassword);
        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}

