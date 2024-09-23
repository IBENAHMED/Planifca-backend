package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.dto.PaymentResponse;
import com.eduAcademy.management_system.dto.StudentRequeste;
import com.eduAcademy.management_system.entity.Payment;
import com.eduAcademy.management_system.entity.Student;
import com.eduAcademy.management_system.enums.Level;
import com.eduAcademy.management_system.service.PaymentService;
import com.eduAcademy.management_system.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final PaymentService paymentService;

    @PostMapping(path = "addStudent")
    public ResponseEntity<?> addStudent(@RequestBody StudentRequeste studentRequeste) {
        studentService.addStudent(studentRequeste);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(path = "allStudents")
    public ResponseEntity<List<Student>> getStudents() {

        List<Student> students = studentService.getAllStudent();
        for (Student student : students) {
            paymentService.updateStudentStatus(student.getId());
        }

        return ResponseEntity.ok(students);
    }

  @GetMapping(path = "{studentId}")   public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        paymentService.updateStudentStatus(studentId);
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }

    @GetMapping(path = "/level/{levelName}")
    public ResponseEntity<List<Student>> getStudentLevelName(@PathVariable String levelName) {
        Level levelEnum = Level.valueOf(levelName.toUpperCase());
        List<Student> students = studentService.getStudentByLevelName(levelEnum);
        return ResponseEntity.ok(students);
    }
}
