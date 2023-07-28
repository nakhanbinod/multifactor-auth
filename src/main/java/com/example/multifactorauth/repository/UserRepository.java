package com.example.multifactorauth.repository;

import com.example.multifactorauth.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    @Modifying
    @Query("update User u set u.secret = :secret where u.email = :email")
    void updateSecretToken(String secret,String email);

}
