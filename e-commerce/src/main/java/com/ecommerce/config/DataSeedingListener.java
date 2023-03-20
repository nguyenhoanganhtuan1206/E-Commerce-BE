package com.ecommerce.config;

import com.ecommerce.persistent.role.RoleEntity;
import com.ecommerce.persistent.role.RoleRepository;
import com.ecommerce.persistent.user.UserEntity;
import com.ecommerce.persistent.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new RoleEntity("ROLE_ADMIN"));
        }

        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(new RoleEntity("ROLE_USER"));
        }

        if (roleRepository.findByName("ROLE_SELLER").isEmpty()) {
            roleRepository.save(new RoleEntity("ROLE_SELLER"));
        }

        final RoleEntity roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElse(null);
        final RoleEntity roleUser = roleRepository.findByName("ROLE_USER").orElse(null);

        // ADD ADMIN
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            UserEntity userEntity = UserEntity.builder()
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .phoneNumber("12323232")
                    .createdAt(Instant.now())
                    .roles(new HashSet<>(Collections.singletonList(roleAdmin)))
                    .build();

            userRepository.save(userEntity);
        }

        // ADD USER
        if (userRepository.findByEmail("user@gmail.com").isEmpty()) {

            UserEntity userEntity = UserEntity.builder()
                    .email("user@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .phoneNumber("12323232")
                    .createdAt(Instant.now())
                    .roles(new HashSet<>(Collections.singletonList(roleUser)))
                    .build();

            userRepository.save(userEntity);
        }
    }
}
