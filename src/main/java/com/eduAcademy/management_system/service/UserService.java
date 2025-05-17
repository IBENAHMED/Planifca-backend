package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.ClubResponseDto;
import com.eduAcademy.management_system.dto.UserDto;
import com.eduAcademy.management_system.dto.UserResponse;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface UserService {
    UserResponse getUserInfo(Authentication authentication);

    Page<UserResponse> getUserList(int page, int size,String clubRef);

    UserResponse register(UserDto request, String clubRef) throws MessagingException, IOException;

    void updateUserByEmail(UserDto request, String email,String clubRef) throws MessagingException, IOException;

    UserResponse getUserByEmail(String email,String clubRef) throws MessagingException, IOException;

    UserResponse getUserByUserId(String userId,String clubRef) throws MessagingException, IOException;



}
