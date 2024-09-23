package com.eduAcademy.management_system.entity;

import com.eduAcademy.management_system.enums.Level;
import com.eduAcademy.management_system.enums.StatusPayment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data @AllArgsConstructor @Builder
@NoArgsConstructor
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;
    private String gender;
    @Column(unique=true)
    private String reference;
    @Enumerated(EnumType.STRING)
    private Level levelName;
    private StatusPayment statusPayment;
    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<Payment> payments;
}
