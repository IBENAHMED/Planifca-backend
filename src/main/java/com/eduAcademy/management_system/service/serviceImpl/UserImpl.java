package com.eduAcademy.management_system.service.serviceImpl;

import com.eduAcademy.management_system.dto.UserDto;
import com.eduAcademy.management_system.dto.UserResponse;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.mapper.UserMapper;
import com.eduAcademy.management_system.repository.UserRepository;
import com.eduAcademy.management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getUserInfo(Authentication authentication) {
        User user=userRepository.findByEmail(authentication.getName()).orElseThrow(() ->
                new NotFoundException("User not found"));
        return userMapper.userToUserResponse(user);
    }
}
