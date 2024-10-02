package com.example.Airbicycle.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter @Setter
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;
    private Long paymentSum;
    private Timestamp createDate;
    private boolean status; // 결제 상태 True -> 결제 완료
}
