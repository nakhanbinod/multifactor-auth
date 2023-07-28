package com.example.multifactorauth.repository;

import com.example.multifactorauth.entity.Role;
import com.example.multifactorauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
    
}
