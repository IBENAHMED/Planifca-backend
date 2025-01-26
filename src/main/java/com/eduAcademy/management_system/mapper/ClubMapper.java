package com.eduAcademy.management_system.mapper;

import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.dto.ClubResponseDto;
import com.eduAcademy.management_system.entity.Club;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClubMapper {
    ClubRequestDto ClubToClubDTO(Club club);

    Club ClubDTOToClub(ClubRequestDto clubRequestDto);

    ClubResponseDto ClubToClubDtoResponse(Club club);




}
