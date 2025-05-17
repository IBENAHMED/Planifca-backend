package com.eduAcademy.management_system.repository;

import com.eduAcademy.management_system.entity.PasswordResetToken;
import com.eduAcademy.management_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordRestTokenRepository extends JpaRepository<PasswordResetToken,Long> {

    boolean existsByUser(User user);

    Optional<PasswordResetToken> findByUser(User user);

    Optional<PasswordResetToken> findByToken(String token);
}
