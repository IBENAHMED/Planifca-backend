package com.eduAcademy.management_system.repository;

import com.eduAcademy.management_system.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club,Long> {

    Optional<Club> findByEmail(String email);

    boolean existsByReference(String name);

    Optional<Club> findByReference(String reference);

    Optional<Club> findByFrontPath(String frontPath);

    Page<Club> findAll(Pageable pageable);

}
