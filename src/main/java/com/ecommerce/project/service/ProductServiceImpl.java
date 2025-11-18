package com.ecommerce.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CategoryDto;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ModelMapper modelMapper;

	@Override
	public ProductDto addProduct(ProductDto productDto, Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		Double specialPrice = productDto.getPrice() - ((productDto.getDiscount() * 0.01) * productDto.getPrice());
		Product product = modelMapper.map(productDto, Product.class);
		product.setCategory(category);
		product.setImage("default.png");
		product.setSpecialPrice(specialPrice);
		Product savedProduct = productRepository.save(product);
		ProductDto dto = modelMapper.map(savedProduct, ProductDto.class);
		dto.setCategoryDto(modelMapper.map(category, CategoryDto.class));
		return dto;
	}

	/*
	 * public ProductResponse getAllProducts() { List<Product> products =
	 * productRepository.findAll(); List<ProductDto> productDtos =
	 * products.stream().map(product->modelMapper.map(product,ProductDto.class)).
	 * toList(); ProductResponse productResponse= new ProductResponse();
	 * productResponse.setContent(productDtos); return productResponse; }
	 */

	// in the we are not category in the dto so setting it manually so that we can
	// get this in the response
	@Override
	public ProductResponse getAllProducts() {
		List<Product> products = productRepository.findAll();
		List<ProductDto> dtos = products.stream().map(product -> {
			ProductDto productDto = modelMapper.map(product, ProductDto.class);
			if (product.getCategory() != null) {
				CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
				productDto.setCategoryDto(categoryDto);
			}
			return productDto;
		}).collect(Collectors.toList());
		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(dtos);
		return productResponse;
	}

	@Override
	public ProductResponse getProductByCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);

		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(products.stream().map(product -> {
			ProductDto productDto = modelMapper.map(product, ProductDto.class);
			if (product.getCategory() != null) {
				CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
				productDto.setCategoryDto(categoryDto);
			}
			return productDto;
		}).toList());
		return productResponse;
	}

}
