package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.PaymentResponse;
import com.eduAcademy.management_system.dto.StudentRequeste;
import com.eduAcademy.management_system.entity.Payment;
import com.eduAcademy.management_system.entity.Student;
import com.eduAcademy.management_system.enums.Level;
import com.eduAcademy.management_system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;


    private String generateUniqueReference() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

     public void addStudent(StudentRequeste studentRequeste){

        if (studentRepository.findByFirstname(studentRequeste.getFirstname()).isPresent() && studentRepository.findByLastname(studentRequeste.getLastname()).isPresent()) {
            throw new IllegalArgumentException("student already exists");
        }

         var student = Student.builder()
                 .firstname(studentRequeste.getFirstname())
                 .lastname(studentRequeste.getLastname())
                 .dateOfBirth(studentRequeste.getDateOfBirth())
                 .gender(studentRequeste.getGender())
                 .levelName(studentRequeste.getLevelName())
                 .reference(generateUniqueReference())
                 .build();
         studentRepository.save(student);
     }

     public List<Student> getAllStudent(){
        return studentRepository.findAll();
     }

     public Student getStudentById(Long id){
        return studentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Student not found"));
     }

     public List<Student> getStudentByLevelName(Level levelName){
        return studentRepository.findByLevelName(levelName);
     }

}
