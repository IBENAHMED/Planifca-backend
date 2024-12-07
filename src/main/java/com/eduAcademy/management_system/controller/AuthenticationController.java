package com.eduAcademy.management_system.controller;


import com.eduAcademy.management_system.dto.AuthenticationRequeste;
import com.eduAcademy.management_system.dto.AuthenticationResponse;
import com.eduAcademy.management_system.dto.ChangePasswordRequest;
import com.eduAcademy.management_system.dto.RegisterRequeste;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "register")
    public ResponseEntity<User> register(@RequestBody RegisterRequeste requeste){
        authenticationService.register(requeste);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(path = "login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequeste authenticate) {
        AuthenticationResponse response = authenticationService.authenticate(authenticate);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "update-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal userConnect){
        authenticationService.changePassword(request,userConnect);
        return ResponseEntity.ok().build();
    }
}
