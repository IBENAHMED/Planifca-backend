package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.ClubRequestDto;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.Clubonfiguration;
import com.eduAcademy.management_system.mapper.ClubMapper;
import com.eduAcademy.management_system.repository.ClubConfigRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConfigurationClientService {

    private static final String GLOBAL_CONFIG_FILE = "src/main/resources/baseEntity.json";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ClubConfigRepository clubConfigRepository;
    private final ClubMapper clubMapper;
    private final GenerateActivationToken  activationToken;
    private final EmailService emailService;

    public Map<String, Object> loadGlobalConfig() throws IOException {
        return objectMapper.readValue(new File(GLOBAL_CONFIG_FILE), Map.class);
    }

    public void createClientConfigFile(ClubRequestDto clubRequestDto) throws IOException, MessagingException {
        Map<String, Object> globalConfig = loadGlobalConfig();

        Map<String, Object> clientConfig = new HashMap<>(globalConfig);

        clientConfig.put("clientName", clubRequestDto.getFirstName() + clubRequestDto.getLastName());
        clientConfig.put("reference", clubRequestDto.getReference());
        clientConfig.put("frontPath", clubRequestDto.getFirstName() + "-" + clubRequestDto.getLastName());
        clientConfig.put("contactEmail", clubRequestDto.getEmail());

        Map<String, Object> accessControl = new HashMap<>();
        accessControl.put("Roles", clubRequestDto.getRoles());
        clientConfig.put("accessControl", accessControl);

        Club club = clubMapper.ClubDTOToClub(clubRequestDto);

        String clientConfigJson = objectMapper.writeValueAsString(clientConfig);

        var clubonfiguration = Clubonfiguration.builder()
                .club(club)
                .value(clientConfigJson)
                .build();
        clubConfigRepository.save(clubonfiguration);

        String token=activationToken.generateActivationToken(club.getEmail(),club.getId());

        String link="http://localhost:4200/activate-account?token="+token;
        System.out.println(token);
        emailService.sendEmail(clubRequestDto.getEmail(),link);
    }



}
