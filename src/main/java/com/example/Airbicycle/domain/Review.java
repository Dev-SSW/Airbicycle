package com.example.Airbicycle.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter@Setter
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 작성자
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "bicycle_id")
    private Bicycle bicycle;
    private String comment;
    private int star; // 별점
    private boolean heart; // 좋아요 True  / 싫어요 False
    private Timestamp createDate;
}
