package com.ecommerce.domain.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class RoleDTO {

    private UUID id;

    private String name;
}
