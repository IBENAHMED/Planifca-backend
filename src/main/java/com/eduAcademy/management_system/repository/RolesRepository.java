package com.eduAcademy.management_system.repository;

import com.eduAcademy.management_system.entity.Roles;
import com.eduAcademy.management_system.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByName(String roleName);
}
