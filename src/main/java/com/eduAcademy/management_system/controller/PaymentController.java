package com.eduAcademy.management_system.controller;

import com.eduAcademy.management_system.dto.PaymentRequeste;
import com.eduAcademy.management_system.dto.PaymentResponse;
import com.eduAcademy.management_system.entity.Payment;
import com.eduAcademy.management_system.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping(path = "savePayment")
    public ResponseEntity<?> savePayment(@RequestBody PaymentRequeste payment) {
        paymentService.savePayment(payment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("allPayments")
    public ResponseEntity<?> getAllPayment(){
        return ResponseEntity.ok(paymentService.getAllPayment());
    }

    @GetMapping(path = "{studentId}/payments")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByStudentId(@PathVariable Long studentId) {
        List<PaymentResponse> payments = paymentService.getPaymentByStudent(studentId);
        if (payments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(payments);
    }

    @GetMapping(path = "{studentId}/unpaid-months")
    public ResponseEntity<List<YearMonth>> getUnpaidMonths(@PathVariable Long studentId) {
        List<YearMonth> unpaidMonths = paymentService.getUnpaidMonths(studentId);
        if (unpaidMonths.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(unpaidMonths);
    }
}
