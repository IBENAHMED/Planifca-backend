package com.eduAcademy.management_system.services;

import com.eduAcademy.management_system.dto.UserResponse;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.mapper.UserMapper;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.repository.UserRepository;
import com.eduAcademy.management_system.service.serviceImpl.EmailService;
import com.eduAcademy.management_system.service.serviceImpl.UserImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class UserImplTest {

    @InjectMocks
    private UserImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private EmailService emailService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserInfo_UserFound() {
        User user = new User();
        user.setEmail("test@example.com");
        UserResponse expectedResponse = new UserResponse();
        expectedResponse.setEmail("test@example.com");

        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userMapper.userToUserResponse(user)).thenReturn(expectedResponse);

        UserResponse result = userService.getUserInfo(authentication);

        assertThat(result.getEmail()).isEqualTo("test@example.com");
        verify(userRepository).findByEmail("test@example.com");
    }
}
