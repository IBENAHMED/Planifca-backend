package com.eduAcademy.management_system.repository;

import com.eduAcademy.management_system.entity.Student;
import com.eduAcademy.management_system.enums.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByFirstname(String firstname);
    Optional<Student> findByLastname(String lastname);
    List<Student> findByLevelName(Level levelName);
}
