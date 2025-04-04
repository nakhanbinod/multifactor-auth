package com.example.multifactorauth.repository;

import com.example.multifactorauth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByVerificationToken(String token);
}
