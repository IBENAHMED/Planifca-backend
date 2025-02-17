package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.AuthenticationRequestDto;
import com.eduAcademy.management_system.dto.AuthenticationResponseDto;
import com.eduAcademy.management_system.dto.RegisterRequestDto;

public interface AuthService {

    void register(RegisterRequestDto request,String clubRef);
    AuthenticationResponseDto authenticate(AuthenticationRequestDto request);
}
