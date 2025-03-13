package com.eduAcademy.management_system.service.serviceImpl;

import com.eduAcademy.management_system.dto.StadiumDto;
import com.eduAcademy.management_system.dto.StadiumResponse;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.Stadium;
import com.eduAcademy.management_system.enums.TypeSport;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.mapper.StadiumMapper;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.repository.StadiumRepository;
import com.eduAcademy.management_system.service.StadiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StadiumImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;
    private final ClubRepository clubRepository;
    private final StadiumMapper stadiumMapper;

    @Override
    public StadiumResponse addStadium(StadiumDto stadiumDto, String clubRef) {
        Club club = clubRepository.findByReference(clubRef)
                .orElseThrow(() -> new NotFoundException("club not found with path <'" + clubRef+"'>"));
        Stadium stadium=stadiumMapper.toEntity(stadiumDto);

        stadium.setClub(club);
        stadium.setName(stadiumDto.getName());
        stadium.setPricePerHour(stadiumDto.getPricePerHour());
        stadium.setTerrainId(generateTerrainId(TypeSport.valueOf(stadiumDto.getTypeSport().toUpperCase())));
        stadium.setTypeSport(TypeSport.valueOf(stadiumDto.getTypeSport().toUpperCase()));
        stadiumRepository.save(stadium);

        stadiumMapper.stadiumToStadiumResponse(stadium);
        return stadiumMapper.stadiumToStadiumResponse(stadium);
    }

    @Override
    public List<StadiumResponse> getStadiumsBySportAndClub(String clubRef, String typeSport) {
        Club club = clubRepository.findByReference(clubRef)
                .orElseThrow(() -> new NotFoundException("Club not found with path <'" + clubRef + "'>"));

        TypeSport sportType;
        try {
            sportType = TypeSport.valueOf(typeSport.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("The sport type '" + typeSport + "' does not exist.");
        }

        List<Stadium> stadiums = stadiumRepository.findByClubAndTypeSport(club, sportType);

        return stadiums.stream()
                .map(stadiumMapper::stadiumToStadiumResponse)
                .toList();
    }

    public String generateTerrainId(TypeSport sportType) {
        return sportType.name().substring(0, 3).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 5);    }
}
