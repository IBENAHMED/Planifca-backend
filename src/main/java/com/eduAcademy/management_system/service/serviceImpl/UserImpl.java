package com.eduAcademy.management_system.service.serviceImpl;

import com.eduAcademy.management_system.dto.UserDto;
import com.eduAcademy.management_system.dto.UserResponse;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.mapper.UserMapper;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.repository.UserRepository;
import com.eduAcademy.management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserImpl implements UserService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getUserInfo(Authentication authentication) {
        User user=userRepository.findByEmail(authentication.getName()).orElseThrow(() ->
                new NotFoundException("User not found"));
        return userMapper.userToUserResponse(user);
    }

    @Override
    public Page<UserResponse> getUserList(int page, int size, String clubRef) {
        Club club = clubRepository.findByReference(clubRef)
                .orElseThrow(() -> new NotFoundException("Club with reference " + clubRef + " not found."));

        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userRepository.findByClub(club, pageable);

        return usersPage.map(userMapper::userToUserResponse);
    }

}
