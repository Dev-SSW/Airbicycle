package com.example.Airbicycle.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "Bicycle")
public class Bicycle {
    @Id@GeneratedValue
    @Column(name = "bicycle_id",nullable = false)
    private Long id;
    @OneToOne
    @JoinColumn(name = "payment_id",nullable = false)
    private Payment paymentId;

    private String bicycleModel;
    private int price;
    private int pricePerHour;
    private String location;
    private boolean available; //사용가능 / 불가능 여부
    private int year; // 연식

}
