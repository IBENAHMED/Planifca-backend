package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.StadiumDto;
import com.eduAcademy.management_system.dto.StadiumResponse;

import java.util.List;

public interface StadiumService {

    StadiumResponse addStadium(StadiumDto stadiumDto, String clubRef);

    List<StadiumResponse> getStadiumsBySportAndClub(String clubRef,String typeSport);
}
