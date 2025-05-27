package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.ActivationRequestDto;
import com.eduAcademy.management_system.dto.PasswordChangeRequest;
import com.eduAcademy.management_system.dto.RestPasswordDto;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;

import java.io.IOException;

public interface UserPasswordService {

     void changeUserPassword(PasswordChangeRequest passwordChangeRequest) throws BadRequestException;

     void forgotPassword(String email, String clubRef) throws MessagingException, IOException;

     void resetPassword(PasswordChangeRequest passwordChangeRequest);

     void activateAccount(ActivationRequestDto requestDto,String userId);

}
