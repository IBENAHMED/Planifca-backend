package com.eduAcademy.management_system.config;

import com.eduAcademy.management_system.entity.Role;
import com.eduAcademy.management_system.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        addRoleIfNotExists("admin");
        addRoleIfNotExists("user");
        addRoleIfNotExists("manager");
    }

    private void addRoleIfNotExists(String roleName) {
        if (!roleRepository.findByName(roleName).isPresent()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
