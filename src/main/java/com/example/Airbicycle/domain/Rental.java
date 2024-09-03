package com.example.Airbicycle.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@Getter @Setter
@Table(name = "Rental")
public class Rental {
    @Id@GeneratedValue
    @Column(name = "rental_id")
    private Long Id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
    @ManyToOne
    @JoinColumn(name = "Bicycle_id")
    private Bicycle bicycleId;

    private Timestamp startTime;
    private Timestamp endTime;

}
