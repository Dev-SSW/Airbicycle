package com.example.Airbicycle.domain;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter@Setter
public class Message {
    @Id @GeneratedValue
    @Column(name = "message_id")
    private Long id;
    @ManyToOne
    private Chat chatId;
    @ManyToOne
    private User userId;
    private String comment;
    private Timestamp sendTime; // 보낸시간
}
