package com.eduAcademy.management_system.mapper;

import com.eduAcademy.management_system.dto.StadiumDto;
import com.eduAcademy.management_system.dto.StadiumResponse;
import com.eduAcademy.management_system.entity.Stadium;
import com.eduAcademy.management_system.enums.TypeSport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StadiumMapper {

    @Mapping(source = "typeSport", target = "typeSport", qualifiedByName = "typeSportToString")
    StadiumDto toDto(Stadium stadium);

    @Mapping(source = "typeSport", target = "typeSport", qualifiedByName = "stringToTypeSport")
    Stadium toEntity(StadiumDto stadiumDTO);

    @Mapping(source = "typeSport", target = "typeSport", qualifiedByName = "typeSportToString")
    StadiumResponse stadiumToStadiumResponse(Stadium stadium);

    @Named("typeSportToString")
    static String typeSportToString(TypeSport sport) {
        return sport != null ? sport.name() : null;
    }

    @Named("stringToTypeSport")
    static TypeSport stringToTypeSport(String sportName) {
        return sportName != null ? TypeSport.valueOf(sportName.toUpperCase()) : null;
    }
}
