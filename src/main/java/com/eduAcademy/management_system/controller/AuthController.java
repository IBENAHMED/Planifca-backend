package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.dto.AuthenticationRequestDto;
import com.eduAcademy.management_system.dto.AuthenticationResponseDto;
import com.eduAcademy.management_system.dto.RegisterRequestDto;
import com.eduAcademy.management_system.dto.UserDto;
import com.eduAcademy.management_system.exception.ConflictException;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.exception.UnauthorizedException;
import com.eduAcademy.management_system.service.serviceImpl.AuthServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
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
    public ResponseEntity<?> register(@RequestBody UserDto request,
                                           @RequestHeader String clubRef) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(authServiceImpl.register(request, clubRef));
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }


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
}

