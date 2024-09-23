package com.eduAcademy.management_system.dto;

import com.eduAcademy.management_system.entity.Student;
import com.eduAcademy.management_system.enums.StatusPayment;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequeste {
    private String monthPayment;
    private StatusPayment status;
    private Long studentId;
}
