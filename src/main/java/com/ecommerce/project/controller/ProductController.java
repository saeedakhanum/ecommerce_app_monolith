package com.ecommerce.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	// add product
	@PostMapping("/admin/categories/{categoryId}/product")
	public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto, @PathVariable Long categoryId) {

		return new ResponseEntity<ProductDto>(productService.addProduct(productDto, categoryId), HttpStatus.CREATED);

	}

	// get all products
	@GetMapping("/public/products")
	public ResponseEntity<ProductResponse> getAllProducts() {
		ProductResponse productResponse = productService.getAllProducts();
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	// get products by catgoryId
	@GetMapping("/public/categories/{categoryId}/products")
	public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId) {
		ProductResponse productResponse = productService.getProductByCategory(categoryId);
		return ResponseEntity.ok(productResponse);

	}
}
