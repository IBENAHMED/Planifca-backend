package com.eduAcademy.management_system.config;

import com.eduAcademy.management_system.entity.Club;
import com.eduAcademy.management_system.entity.Roles;
import com.eduAcademy.management_system.entity.User;
import com.eduAcademy.management_system.enums.RoleName;
import com.eduAcademy.management_system.repository.ClubRepository;
import com.eduAcademy.management_system.repository.RolesRepository;
import com.eduAcademy.management_system.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ClubRepository clubRepository;
    private final RolesRepository rolesRepository;

    public DataInitializer(ClubRepository clubRepository,RolesRepository rolesRepository) {
        this.clubRepository = clubRepository;
        this.rolesRepository = rolesRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        createSpaceAdmin();
        initRoles();
    }

    private void createSpaceAdmin() {
        clubRepository.findByReference("PLSA").ifPresentOrElse(
                club -> System.out.println("⚠️ Space Admin Club already exists. Skipping creation."),
                () -> {
                    Club newClub = new Club();
                    newClub.setReference("PLSA");
                    newClub.setName("space clients");
                    newClub.setEmail("club@admin.com");
                    newClub.setFrontPath("space-clients");
                    newClub.setLogo("/uploads/PlanifcaLogo.png");
                    newClub.setActive(true);

                    clubRepository.save(newClub);
                    System.out.println("✅ Space Admin Club created successfully!");
                }
        );
    }

    public void initRoles() {
        List<String> roleNames = Arrays.asList("ADMIN", "STAFF", "SUPERADMIN");

        for (String roleName : roleNames) {
            rolesRepository.findByName(roleName)
                    .orElseGet(() -> rolesRepository.save(new Roles(null, roleName)));
        }

    }
}
