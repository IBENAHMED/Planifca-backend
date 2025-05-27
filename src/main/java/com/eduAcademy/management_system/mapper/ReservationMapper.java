package com.eduAcademy.management_system.mapper;

import com.eduAcademy.management_system.dto.ReservationDto;
import com.eduAcademy.management_system.dto.ReservationResponseDto;
import com.eduAcademy.management_system.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReservationMapper {

    Reservation toReservation(ReservationDto reservationDto);

    ReservationDto toReservationDto(Reservation reservation);

    @Mapping(source = "club.reference", target = "clubReference")
    @Mapping(source = "reservation.stadium.terrainId", target = "stadium.terrainId")
    @Mapping(source = "reservation.stadium.typeSport", target = "stadium.typeSport")
    @Mapping(source = "reservation.stadium.pricePerHour", target = "stadium.pricePerHour")
    ReservationResponseDto toReservationResponseDto(Reservation reservation);
}
