package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.ClubResponseDto;
import com.eduAcademy.management_system.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface UserService {
    UserResponse getUserInfo(Authentication authentication);
    Page<UserResponse> getUserList(int page, int size,String clubRef);


}
