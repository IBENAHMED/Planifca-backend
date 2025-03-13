package com.eduAcademy.management_system.repository;

import com.eduAcademy.management_system.dto.UserResponse;
import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndClubReference(String email, String clubRef);
    Optional<User> findByEmail(String email);
    Page<User> findByClub(Club club, Pageable pageable);
}
