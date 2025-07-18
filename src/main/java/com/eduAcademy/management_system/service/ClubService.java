package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.dto.ClubResponseDto;
import org.springframework.data.domain.Page;

import java.io.IOException;


public interface ClubService {
    ClubResponseDto createClub(ClubRequestDto requestDto) throws IOException;
    String generateUniqueReference(String name);
    ClubResponseDto getClubByFrontPath(String frontPath);
    void updateClub(ClubRequestDto requestDto,String clubRef);
    void deleteClub(ClubRequestDto requestDto);
    Page<ClubResponseDto> getClubs(int page, int size);
}
