package com.example.Airbicycle.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
public class Chat {
    @Id @GeneratedValue
    @Column(name = "chat_id")
    private Long id;
    @ManyToOne
    private User userId;

}
