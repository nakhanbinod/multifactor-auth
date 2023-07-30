package com.example.multifactorauth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String verificationToken;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public Token(User user) {
        this.user = user;
        createdAt = new Date();
        verificationToken = UUID.randomUUID().toString();
    }

}
