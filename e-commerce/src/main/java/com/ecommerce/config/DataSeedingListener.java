package com.ecommerce.config;

import com.ecommerce.persistent.paymentMethod.PaymentMethodEntity;
import com.ecommerce.persistent.paymentMethod.PaymentMethodRepository;
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

    private final PaymentMethodRepository paymentMethodRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        seedRoleAndUserData();
        seedPaymentMethod();
    }

    private void seedRoleAndUserData() {
        /**
         * @ Add about User and Role
         * */

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

        addNewUser("admin@gmail.com", "123456", "12323232", Instant.now(), roleAdmin);
        addNewUser("user@gmail.com", "123456", "12323232", Instant.now(), roleUser);
    }

    public void seedPaymentMethod() {
        /**
         * @ Add about payment method
         * */

        if (paymentMethodRepository.findByName("COD").isEmpty()) {
            paymentMethodRepository.save(new PaymentMethodEntity("COD"));
        }

        if (paymentMethodRepository.findByName("Paypal").isEmpty()) {
            paymentMethodRepository.save(new PaymentMethodEntity("Paypal"));
        }
    }

    private void addNewUser(final String email, final String password, final String phoneNumber, final Instant createdAt, final RoleEntity role) {
        if (userRepository.findByEmail(email).isEmpty()) {
            final UserEntity userEntity = new UserEntity();
            userEntity.setEmail(email);
            userEntity.setPassword(passwordEncoder.encode(password));
            userEntity.setPhoneNumber(phoneNumber);
            userEntity.setCreatedAt(createdAt);
            userEntity.setRoles(new HashSet<>(Collections.singletonList(role)));

            userRepository.save(userEntity);
        }
    }
}
