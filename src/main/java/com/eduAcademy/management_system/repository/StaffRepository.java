package com.eduAcademy.management_system.repository;

import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByEmailAndClub(String email, Club club);
    Optional<Staff> findByEmail(String email);
    boolean existsByObjectId(String objectId);
    Optional<Staff> findByObjectId(String objectId);
    void deleteByObjectId(String objectId);
}
