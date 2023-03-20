package com.ecommerce.persistent.role;

import com.ecommerce.persistent.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany(mappedBy = "roles",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.ALL})
    private Set<UserEntity> users;

    public RoleEntity(String name) {
        this.name = name;
    }
}
