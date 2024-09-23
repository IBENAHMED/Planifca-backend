package com.eduAcademy.management_system.dto;

import com.eduAcademy.management_system.enums.StatusPayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;

@Data
@AllArgsConstructor @NoArgsConstructor
public class PaymentResponse {
    private LocalDateTime paymentDate;
    private YearMonth monthPaymentDate;
    private StatusPayment status;
}
