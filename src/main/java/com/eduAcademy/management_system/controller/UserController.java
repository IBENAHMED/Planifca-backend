package com.eduAcademy.management_system.controller;


import com.eduAcademy.management_system.dto.UserResponse;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("getUserList")
    public Page<UserResponse> getClubs(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size
            , @RequestHeader String clubRef
    ) {
        return userService.getUserList(page, size,clubRef);
    }
}
