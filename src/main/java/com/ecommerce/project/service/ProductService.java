package com.ecommerce.project.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;

public interface ProductService {

	ProductDto addProduct(ProductDto productDto, Long categoryId);
	
	public ProductResponse getAllProducts();

	public ProductResponse getProductByCategory(Long categoryId);

	ProductResponse searchProductByKeyword(String keyword);
	
	ProductDto updateProduct(ProductDto productDto,Long categoryId, Long productId);
	
	ProductDto getProductByProductId(Long productId);
	
	void deleteProduct(Long productId);
	
	ProductDto updateProductImage(Long productId,MultipartFile image) throws IOException;
}
