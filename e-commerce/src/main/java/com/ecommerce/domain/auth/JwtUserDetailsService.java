package com.ecommerce.domain.auth;

import com.ecommerce.persistent.role.RoleRepository;
import com.ecommerce.persistent.user.UserEntity;
import com.ecommerce.persistent.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).map(user -> buildUser(user)).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    private User buildUser(final UserEntity userEntity) {
        return new JwtUserDetails(userEntity.getId(), userEntity.getEmail(), userEntity.getPassword(), List.of(new SimpleGrantedAuthority(roleRepository.findById(userEntity.getRole().getId()).get().getName())));
    }
}
