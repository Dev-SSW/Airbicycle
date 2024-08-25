package com.example.Airbicycle.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter @Setter
public class Payment {
    @Id @GeneratedValue
    @Column(name = "payment_id")
    private Long id;

    private Long paymentSum;
    private Timestamp createDate;
    private boolean status; // 결제 상태 True -> 결제 완료
}
