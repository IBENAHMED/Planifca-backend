package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.dto.ActivationAccountRequestDto;
import com.eduAcademy.management_system.service.ActivationAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal")
public class ActivationAccountController {
    private final ActivationAccountService activationAccountService;

    @PostMapping("activate-account")
    public ResponseEntity<String> activateAccount(@RequestBody ActivationAccountRequestDto activationRequest) {
        try {
            activationAccountService.activateAccount(activationRequest.getPassword(),activationRequest.getConfirmPassword(),activationRequest.getToken(),activationRequest.getEntityType());
            return ResponseEntity.ok("Account activated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
