package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.dto.*;
import com.eduAcademy.management_system.exception.BadRequestException;
import com.eduAcademy.management_system.exception.ConflictException;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.exception.UnauthorizedException;
import com.eduAcademy.management_system.service.UserPasswordService;
import com.eduAcademy.management_system.service.serviceImpl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@RestController
@RequestMapping("/api/internal/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    private final UserPasswordService userPasswordService;

    @PostMapping(path = "login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto authenticate ,@RequestHeader String clubRef) {
        try {
            AuthenticationResponseDto response = authServiceImpl.authenticate(authenticate,clubRef);
            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage());
        }catch (NotFoundException e) {
              throw new NotFoundException(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthenticationResponseDto("An internal error has occurred."));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        try {
            userPasswordService.changeUserPassword(request);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        }catch (com.eduAcademy.management_system.exception.BadRequestException e) {
            throw new com.eduAcademy.management_system.exception.BadRequestException(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody RestPasswordDto restPasswordDto, @RequestHeader String clubRef){
        try {
            userPasswordService.forgotPassword(restPasswordDto.getEmail(),clubRef);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordChangeRequest request) {
        try {
            userPasswordService.resetPassword(request);
            return ResponseEntity.ok().build();
        } catch (com.eduAcademy.management_system.exception.BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestBody ActivationRequestDto requestDto,@RequestParam String userId) {

        try {
            userPasswordService.activateAccount(requestDto,userId);
            return ResponseEntity.ok("Your account has been activated successfully.");
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (ConflictException e){
            throw new ConflictException(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}

