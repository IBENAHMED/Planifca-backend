package com.eduAcademy.management_system.service.serviceImpl;

import com.eduAcademy.management_system.dto.UserDto;
import com.eduAcademy.management_system.dto.UserResponse;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.exception.ConflictException;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.mapper.UserMapper;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.repository.UserRepository;
import com.eduAcademy.management_system.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserImpl implements UserService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;

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

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse register(UserDto request, String clubRef) throws MessagingException, IOException {
        Club club = clubRepository.findByReference(clubRef)
                .orElseThrow(() -> new NotFoundException("Club with reference <" + clubRef + "> not found"));

        if (userRepository.findByEmailAndClubReference(request.getEmail(), clubRef).isPresent()) {
            throw new ConflictException("The email <" + request.getEmail() + "> is already in use");
        }


        UserDto userDto = UserDto.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .roles(request.getRoles())
                .club(club)
                .build();

        User user= userMapper.userDtoToUser(userDto);
        user.setUserId(generateUserId());
        userRepository.save(user);
        emailService.sendEmailActivationAccount(request.getEmail(),request.getFirstName(),request.getLastName(),user.getUserId());

        return userMapper.userToUserResponse(user);
    }

    @Override
    public void updateUserByEmail(UserDto request, String email, String clubRef) throws MessagingException, IOException {
        Club club=clubRepository.findByReference(clubRef)
                .orElseThrow(()->new NotFoundException("club not found"));
       userRepository.findByEmailAndClubReference(email,clubRef)
                .orElseThrow(()->new NotFoundException("User not found"));

        UserDto userDto = UserDto.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .club(club)
                .roles(request.getRoles())
                .build();

        User user= userMapper.userDtoToUser(userDto);
        userRepository.save(user);

    }

    @Override
    public UserResponse getUserByEmail(String email,String clubRef) throws MessagingException, IOException {
        User user=userRepository.findByEmailAndClubReference(email,clubRef)
                .orElseThrow(()->new NotFoundException("User not found with email  <" + email + ">"));
        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse getUserByUserId(String userId,String clubRef) throws MessagingException, IOException {
        User user=userRepository.findByUserIdAndClubReference(userId,clubRef)
                .orElseThrow(()->new NotFoundException("User not found with email  <" + userId + ">"));
        return userMapper.userToUserResponse(user);
    }

    private String generateUserId() {
        Random random = new Random();

        int segment1 = random.nextInt(90) + 10;
        int segment2 = random.nextInt(90) + 10;
        int segment3 = random.nextInt(90) + 10;
        int segment4 = random.nextInt(90) + 10;

        return String.format("%02d:%02d:%02d:%02d", segment1, segment2, segment3, segment4);
    }

}
