package com.eduAcademy.management_system.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ClientConfigDto {
    private String contactEmail;
    private String clientName;
    private String frontPath;
    private String reference;
    private Map<String, Object> features;
    private Map<String, Object> accessControl;
}
