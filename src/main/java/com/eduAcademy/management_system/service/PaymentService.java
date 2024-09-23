package com.eduAcademy.management_system.service;

import com.eduAcademy.management_system.dto.PaymentRequeste;
import com.eduAcademy.management_system.dto.PaymentResponse;
import com.eduAcademy.management_system.entity.Payment;
import com.eduAcademy.management_system.entity.Student;
import com.eduAcademy.management_system.enums.StatusPayment;
import com.eduAcademy.management_system.repository.PaymentRepository;
import com.eduAcademy.management_system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;

    public void savePayment(PaymentRequeste requeste) {

        Student student=studentRepository.findById(requeste.getStudentId()).orElseThrow(()->
                new IllegalStateException("Student not found"));
        Payment paymente=new Payment();

        YearMonth monthPaymentDate = YearMonth.parse(requeste.getMonthPayment(), DateTimeFormatter.ofPattern("yyyy-MM"));

        var payment=Payment.builder()
                .monthPayment(monthPaymentDate)
                .student(student)
                .paymentDate(LocalDateTime.now())
                .status(requeste.getStatus())
                .build();
        System.out.println(payment.getMonthPayment().getMonth());
        paymentRepository.save(payment);
    }

   public List<Payment> getAllPayment(){
        return paymentRepository.findAll();
   }

    public List<PaymentResponse> getPaymentByStudent(Long studentId){
        Student student = studentRepository.findById(studentId).orElseThrow(()->
                new IllegalArgumentException("student not found"));

        return student.getPayments().stream()
                .map(payment -> new PaymentResponse(

                        payment.getPaymentDate(),
                        payment.getMonthPayment(),
                        payment.getStatus()
                ))
                .collect(Collectors.toList());
    }

    public List<YearMonth> getUnpaidMonths(Long studentId){
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));


        List<YearMonth> paidMonths = student.getPayments().stream()
                .map(Payment::getMonthPayment)
                .collect(Collectors.toList());

        YearMonth startMonth = YearMonth.of(LocalDate.now().getYear(), 9);
        YearMonth endMonth = YearMonth.of(LocalDate.now().getYear() + 1, 5);

        List<YearMonth> allMonths = new ArrayList<>();


        YearMonth currentMonth = startMonth;
        while (!currentMonth.isAfter(endMonth)) {
            if (currentMonth.isBefore(YearMonth.now().plusMonths(1))) {
                allMonths.add(currentMonth);
            }
            currentMonth = currentMonth.plusMonths(1); // Passe au mois suivant
        }

        return allMonths.stream()
                .filter(month -> !paidMonths.contains(month))
                .collect(Collectors.toList());
    }


    public void updateStudentStatus(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        List<YearMonth> unpaidMonths = getUnpaidMonths(studentId);

        if (unpaidMonths.contains(YearMonth.now()) ) {
            student.setStatusPayment(StatusPayment.IMPAYES);
        } else {
            student.setStatusPayment(StatusPayment.PAYE);
        }

        studentRepository.save(student);
    }

}
