package com.ecommerce.project.service;

import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;

public interface ProductService {

	ProductDto addProduct(ProductDto productDto, Long categoryId);
	
	public ProductResponse getAllProducts();

	public ProductResponse getProductByCategory(Long categoryId);
}
