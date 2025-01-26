package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.dto.ClubResponseDto;
import com.eduAcademy.management_system.entity.Club;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ClubServiceInterface {
    ClubRequestDto createClub(ClubRequestDto clubRequestDto) throws MessagingException, IOException;
    String generateUniqueReference();
    Page<Club> getClubs(int size, int page);
    ClubResponseDto updateClub(String clubRef,ClubRequestDto clubRequest) throws MessagingException;
}
