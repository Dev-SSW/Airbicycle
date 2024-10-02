package com.example.Airbicycle.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;

import java.sql.Timestamp;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Table(name = "Bicycle")
public class Bicycle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bicycle_id",nullable = false)
    private Long id;
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "payment_id",nullable = false)
    private Payment payment;
    private String bicycleModel;
    private int price;
    private int pricePerHour;
    private String location;
    private boolean available; //사용가능 / 불가능 여부
    private int year; // 연식

}
