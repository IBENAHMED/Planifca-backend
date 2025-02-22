package com.eduAcademy.management_system.service.serviceImpl;

import com.eduAcademy.management_system.entity.Roles;
import com.eduAcademy.management_system.enums.RoleName;
import com.eduAcademy.management_system.exception.NotFoundException;
import com.eduAcademy.management_system.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RolesRepository rolesRepository;

    public Roles findByRoleName(String roleName) {
        return rolesRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Rôle non trouvé : " + roleName));
    }
}
