package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.StaffRequestDto;
import com.eduAcademy.management_system.dto.StaffResponseDto;
import jakarta.mail.MessagingException;

public interface StaffServiceInterface {
    void addStaff(StaffRequestDto staffRequestDto, String clubRef) throws MessagingException;
    StaffResponseDto updateStaff(StaffRequestDto staffRequestDto,String email,String clubRef);
    void deleteStaff(String objectId);

}
