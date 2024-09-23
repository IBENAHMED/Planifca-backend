package com.eduAcademy.management_system.repository;

import com.eduAcademy.management_system.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
