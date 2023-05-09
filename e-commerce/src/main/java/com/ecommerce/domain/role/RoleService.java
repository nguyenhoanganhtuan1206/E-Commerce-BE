package com.ecommerce.domain.role;

import com.ecommerce.persistent.role.RoleEntity;
import com.ecommerce.persistent.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ecommerce.domain.role.RoleError.supplyRoleNotFound;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleEntity findByName(final String name) {
        return roleRepository.findByName(name)
                .orElseThrow(supplyRoleNotFound(name));
    }
}
