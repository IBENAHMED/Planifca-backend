package com.eduAcademy.management_system.mapper;

import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.dto.ClubResponseDto;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.service.serviceImpl.ClubServiceImpl;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClubMapper {

    ClubResponseDto toClubResponseDto(Club club);

    Club toClub(ClubRequestDto clubRequestDto);
}
