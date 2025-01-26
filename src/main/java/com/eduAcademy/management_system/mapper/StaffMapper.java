package com.eduAcademy.management_system.mapper;

import com.eduAcademy.management_system.dto.StaffRequestDto;
import com.eduAcademy.management_system.dto.StaffResponseDto;
import com.eduAcademy.management_system.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StaffMapper {

    StaffRequestDto staffToStaffRequestDto(Staff staff);

    Staff StaffDtoToStaff(StaffRequestDto staffRequestDto);

    StaffResponseDto staffToStaffResponseDto(Staff staff);

}
