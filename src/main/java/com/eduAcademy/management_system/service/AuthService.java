package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.*;

public interface AuthService {

    UserResponse register(UserDto request, String clubRef);
    AuthenticationResponseDto authenticate(AuthenticationRequestDto request,String clubRef);
}
