package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.ClubDTO;
import com.eduAcademy.management_system.dto.ConfirmPassword;
import jakarta.mail.MessagingException;

public interface ClubServiceInterface {
    ClubDTO createClub(ClubDTO clubDTO) throws MessagingException;
    void activateAccount(ConfirmPassword request);
    String generateUniqueReference();
}
