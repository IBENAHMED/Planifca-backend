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

    // Transformer l'entité Club en DTO de réponse
   //@Mapping(target = "logo", source = "logo")
    ClubResponseDto toClubResponseDto(Club club);

    // Transformer le DTO de requête en entité Club
    //@Mapping(target = "logo", expression = "java(mapMultipartFileToString(clubRequestDto.getLogo(), fileStorageService))")
    Club toClub(ClubRequestDto clubRequestDto);

}
