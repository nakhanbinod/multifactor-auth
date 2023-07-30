package com.example.multifactorauth.entity;

import com.example.multifactorauth.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String status = Status.ACTIVE.getValue();
    private boolean enableMfa;
    private String secret;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private boolean enable;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
