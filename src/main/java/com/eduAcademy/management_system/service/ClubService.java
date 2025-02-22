package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.dto.ClubResponseDto;
import com.eduAcademy.management_system.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;


public interface ClubService {
    ClubResponseDto createClub(ClubRequestDto requestDto) throws IOException;
    String generateUniqueReference(String name);
    ClubResponseDto getClubByFrontPath(String frontPath);
    void updateClub(ClubRequestDto requestDto);
    void deleteClub(ClubRequestDto requestDto);
    Page<ClubResponseDto> getClubs(int page, int size);
}
