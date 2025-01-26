package com.eduAcademy.management_system.controller;


import com.eduAcademy.management_system.dto.AuthenticationRequestDto;
import com.eduAcademy.management_system.dto.AuthenticationResponseDto;
import com.eduAcademy.management_system.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    public void testLogin() throws Exception {
        // Données de test
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto("username", "password");
        AuthenticationResponseDto responseDto = new AuthenticationResponseDto("token");

        // Mock du service
        Mockito.when(authenticationService.authenticate(Mockito.any(AuthenticationRequestDto.class)))
                .thenReturn(responseDto);

        // Requête JSON
        String requestJson = """
                {
                    "username": "arena.vileveret@gmail.com",
                    "password": "123456"
                }
                """;

        // Appel du MockMvc
        ResultActions token = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));

        // Vérification
        Mockito.verify(authenticationService, Mockito.times(1)).authenticate(Mockito.any(AuthenticationRequestDto.class));
    }
}
