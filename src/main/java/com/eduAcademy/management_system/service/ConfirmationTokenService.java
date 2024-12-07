package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.ConfirmationToken;
import com.eduAcademy.management_system.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken saveToken(Club club) {
        ConfirmationToken confirmationToken=new ConfirmationToken();
        String token= UUID.randomUUID().toString();
        confirmationToken.setToken(token);
        confirmationToken.setClub(club);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiredAt(LocalDateTime.now().plusDays(5));
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken;
    }


}
