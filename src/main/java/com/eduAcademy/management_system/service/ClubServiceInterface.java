package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.entity.Club;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubServiceInterface {
    ClubRequestDto createClub(ClubRequestDto clubRequestDto) throws MessagingException;
    void activateAccount(String password,String confirmPassword,String token);
    String generateUniqueReference();
    Page<Club> getClubs(int size, int page);
}
