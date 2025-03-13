package com.eduAcademy.management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StadiumResponse {
    private String name;
    private String typeSport;
    private int pricePerHour;
    private String terrainId;
}
