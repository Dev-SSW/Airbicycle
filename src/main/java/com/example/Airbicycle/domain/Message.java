package com.example.Airbicycle.domain;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter@Setter
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String comment;
    private Timestamp sendTime; // 보낸시간
}
