package com.ecommerce.domain.role;

import com.ecommerce.persistent.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ecommerce.domain.role.RoleError.supplyRoleNotFound;
import static com.ecommerce.domain.role.mapper.RoleDTOMapper.toRoleDTO;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleDTO findByName(final String name) {
        return toRoleDTO(roleRepository.findByName(name)
                .orElseThrow(supplyRoleNotFound(name)));
    }
}
