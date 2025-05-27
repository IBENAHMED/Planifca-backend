package com.eduAcademy.management_system.dto;

import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.enums.TypeSport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StadiumDto {
    private String name;
    private String typeSport;
    private int pricePerHour;
    private String terrainId;
    private Club club;
}
