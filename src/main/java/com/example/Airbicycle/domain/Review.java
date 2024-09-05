package com.example.Airbicycle.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Getter@Setter
public class Review {
    @Id@GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    private User userId; // 작성자
    @ManyToOne
    private Bicycle bicycleId; //
    private String comment;
    private int star; // 별점
    private boolean heart; // 좋아요 True  / 싫어요 False
    private Timestamp createDate;
}
