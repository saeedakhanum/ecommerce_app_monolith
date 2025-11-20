package com.ecommerce.project.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto
{
    private Long productId;
    
    @NotEmpty
    @Size(min = 3, message = "product name must contain atleast 3 characters")
    private String productName;

    private String image;

    @NotEmpty
    @Size(min = 6, message = "product description must contain atleast 6 characters")
    private String description;

    @NotNull(message = "quantity cannot be null")
    private Integer quantity;

    @NotNull(message = "price cannot be null")
    private Double price;
    
    @NotNull(message = "discount cannot be null")
    private Double discount;

    private Double specialPrice;
    
    private CategoryDto categoryDto;
}
