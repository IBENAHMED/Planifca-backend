package com.eduAcademy.management_system.mapper;

import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.dto.ClubResponseDto;
import com.eduAcademy.management_system.entity.Club;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClubMapper {
    ClubRequestDto ClubToClubDTO(Club club);

    Club ClubDTOToClub(ClubRequestDto clubRequestDto);

    ClubResponseDto ClubToClubDtoResponse(Club club);




}
