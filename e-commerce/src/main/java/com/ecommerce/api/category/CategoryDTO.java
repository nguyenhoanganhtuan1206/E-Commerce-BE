package com.ecommerce.api.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {

    private UUID id;

    private String categoryName;
}
