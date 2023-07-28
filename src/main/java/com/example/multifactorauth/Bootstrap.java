package com.example.multifactorauth;

import com.example.multifactorauth.entity.Role;
import com.example.multifactorauth.entity.User;
import com.example.multifactorauth.enums.RoleEnum;
import com.example.multifactorauth.enums.Status;
import com.example.multifactorauth.repository.RoleRepository;
import com.example.multifactorauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Bootstrap implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("INSERTING ROLES.....");
        for (RoleEnum roleEnum : RoleEnum.values()) {
            Role role = roleRepository.findByName(roleEnum.getValue());
            if (role == null) {
                roleRepository.save(new Role(roleEnum));
            }
        }

        LOGGER.info("INSERTING ADMIN USER.....");
        if (userRepository.findByEmail("admin@gmail.com") == null){
            User user = new User();
            user.setName("Administrator");
            user.setEmail("admin@gmail.com");
            user.setPassword(new BCryptPasswordEncoder().encode("123"));
            user.setRole(roleRepository.findByName(RoleEnum.ROLE_ADMIN.getValue()));
            user.setStatus(Status.ACTIVE.getValue());
            user.setEnableMfa(false);
            userRepository.save(user);
        }
    }
}
