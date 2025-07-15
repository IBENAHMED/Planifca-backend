package com.eduAcademy.management_system.repository;

import com.eduAcademy.management_system.dto.StadiumResponse;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.Stadium;
import com.eduAcademy.management_system.enums.TypeSport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StadiumRepository extends JpaRepository<Stadium, Long> {
    List<StadiumResponse> findByTypeSport(TypeSport typeSport);
    List<Stadium> findByClubAndTypeSport(Club club, TypeSport typeSport);
    Optional<Stadium> findByTerrainId(String terrainId);
    Optional<Stadium> findByTerrainIdAndClub(String terrainId, Club clubRef);
}
