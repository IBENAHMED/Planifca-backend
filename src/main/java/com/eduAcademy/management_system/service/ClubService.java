package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.dto.ClubResponseDto;
import com.eduAcademy.management_system.entity.Club;

import java.io.IOException;
import java.util.List;

public interface ClubService {
    ClubResponseDto createClub(ClubRequestDto requestDto) throws IOException;
    String generateUniqueReference(String name);
    ClubResponseDto getClubByFrontPath(String frontPath);
    void updateClub(ClubRequestDto requestDto);
    void deleteClub(ClubRequestDto requestDto);
    List<Club> getClubs();
}
