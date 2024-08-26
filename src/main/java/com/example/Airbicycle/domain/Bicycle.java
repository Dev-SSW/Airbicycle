package com.example.Airbicycle.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "Bicycle")
public class Bicycle {
    @Id@GeneratedValue
    @Column(name = "bicycle_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "username",referencedColumnName = "username")
    private User username;
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment paymentId;

    private Long price;
    private Timestamp usingTime;
    private Timestamp startTime;
    private Timestamp endTime;

    private Timestamp extensions; // 연장시간
    private Boolean available; // 사용여부  True -> 사용가능

    



}
