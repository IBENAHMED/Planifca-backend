package com.eduAcademy.management_system.repository;

import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.ClubConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubConfigRepository extends JpaRepository<ClubConfiguration, Long> {

}
