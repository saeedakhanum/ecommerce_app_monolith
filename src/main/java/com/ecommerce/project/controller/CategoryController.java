package com.ecommerce.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.CategoryDto;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {
	private CategoryService categoryService;

	@GetMapping("/echo")
	public ResponseEntity<String> echoMessage(
			@RequestParam(defaultValue = "Hello Word", required = false) String message) {
		return ResponseEntity.ok("Echoed message" + message);
	}

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping("/public/categories/{categoryId}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long categoryId){
		return ResponseEntity.ok(categoryService.getCatgeory(categoryId));
	}
	// get all categories
	@GetMapping("/public/categories")
	public ResponseEntity<CategoryResponse> getAllCategories(
			@RequestParam(name="PageNumber",defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name="sortBy",defaultValue=AppConstants.SORT_CATEGORIES_BY,required=false) String sortBy,
			@RequestParam(name="sortDir",defaultValue=AppConstants.SORT_DIR,required=false) String sortDir
					
			) {
		return ResponseEntity.ok(categoryService.getAllCategories(pageNumber, pageSize,sortBy,sortDir));
	}

	@PostMapping("/public/categories")
	public ResponseEntity<CategoryDto> addCategory(@Valid @RequestBody CategoryDto categoryDto) {
		CategoryDto dto = categoryService.addCategory(categoryDto);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/admin/categories/{categoryId}")
	public ResponseEntity<CategoryDto> deleteCategory(@PathVariable Long categoryId) {
		CategoryDto deletedDto = categoryService.deleteCategory(categoryId);
		return ResponseEntity.status(HttpStatus.OK).body(deletedDto);
	}

	@PutMapping("/api/categories/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,
			@PathVariable Long categoryId) {
		CategoryDto dto = categoryService.updateCategory(categoryDto, categoryId);
		return ResponseEntity.ok(dto);

	}

}
