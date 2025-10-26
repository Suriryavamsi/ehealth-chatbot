package org.ehealth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    private String sender; // user or bot
    private String intent;
    private String entities;
    private Double confidence;
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();

}

