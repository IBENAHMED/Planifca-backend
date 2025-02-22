package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.UserResponse;
import org.springframework.security.core.Authentication;

public interface UserService {
    UserResponse getUserInfo(Authentication authentication);
}
