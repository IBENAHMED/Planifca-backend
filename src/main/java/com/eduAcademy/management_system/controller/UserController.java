package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/user")
public class UserController {
    private final UserService userService;

    @GetMapping("current")
    public ResponseEntity<?> getUserInfo(Authentication authentication){
        try {
            return ResponseEntity.ok(userService.getUserInfo(authentication));
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An internal error has occurred.");
        }
    }
}
