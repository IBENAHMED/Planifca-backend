package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.*;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface AuthService {

    AuthenticationResponseDto authenticate(AuthenticationRequestDto request,String clubRef);
}
