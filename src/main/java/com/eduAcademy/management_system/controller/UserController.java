package com.eduAcademy.management_system.controller;


import com.eduAcademy.management_system.dto.UserDto;
import com.eduAcademy.management_system.dto.UserResponse;

import com.eduAcademy.management_system.exception.ConflictException;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.service.UserService;
import com.eduAcademy.management_system.service.UserPasswordService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/user")
public class UserController {
    private final UserService userService;
    private final UserPasswordService userPasswordService;

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

    @GetMapping("getUserList")
    public Page<UserResponse> getClubs(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size
            , @RequestHeader String clubRef
    ) {
        return userService.getUserList(page, size,clubRef);
    }

    @PostMapping(path = "register")
    public ResponseEntity<?> register(@RequestBody UserDto request,
                                      @RequestHeader String clubRef) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request, clubRef));
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("update/{email}")
    public ResponseEntity<?> updateUser(@RequestBody UserDto request,@PathVariable String email, @RequestHeader String clubRef){
        try {
            userService.updateUserByEmail(request,email,clubRef);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            throw  new NotFoundException(e.getMessage());
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email, @RequestHeader String clubRef){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByEmail(email,clubRef));
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("id/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId, @RequestHeader String clubRef){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByUserId(userId,clubRef));
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
