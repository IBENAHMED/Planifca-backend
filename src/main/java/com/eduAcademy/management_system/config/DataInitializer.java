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
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ClubRepository clubRepository;
    private final RolesRepository rolesRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ClubRepository clubRepository, RolesRepository rolesRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.clubRepository = clubRepository;
        this.rolesRepository = rolesRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        createSpaceAdmin();
        initRoles();
        createUser();
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

    private void createUser() {
        userRepository.findByEmail("admin@planifca.com").ifPresentOrElse(
                user -> System.out.println("⚠️user admin already exists. Skipping creation."),
                ()->{

                    Club club=clubRepository.findByReference("PLSA")
                            .orElseThrow(() -> new RuntimeException("clubRef not found"));

                    Roles adminRole = rolesRepository.findByName("ADMIN")
                            .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

                    Set<Roles> roles = Set.of(adminRole);

                    User newUser = new User();
                    newUser.setEmail("admin@planifca.com");
                    newUser.setUserId("1:23:19:3");
                    newUser.setClub(club);
                    newUser.setFirstName("admin");
                    newUser.setLastName("admin");
                    newUser.setPassword(passwordEncoder.encode("planifca@123"));
                    newUser.setRoles(roles);
                    newUser.setActive(true);
                    userRepository.save(newUser);
                    System.out.println("✅ user Admin  created successfully!");
                }
        );
    }
}
