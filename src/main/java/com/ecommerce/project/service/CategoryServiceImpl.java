package com.ecommerce.project.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDto;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

//	private List<Category> categories = new ArrayList<>();
//	private Long nextId = 1L;

	private CategoryRepository categoryRepository;
	@Autowired
	private ModelMapper modelMapper;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public CategoryResponse getAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		if (categories.isEmpty())
			throw new APIException("No category created till now");
		List<CategoryDto> list = categories.stream().map(category -> modelMapper.map(category, CategoryDto.class))
				.toList();
		CategoryResponse categoryResponse = new CategoryResponse();
		categoryResponse.setContent(list);
		return categoryResponse;
	}

	@Override
	public CategoryDto addCategory(CategoryDto categoryDto) {
//		category.setCategoryId(nextId++);
//		categories.add(category);

		Category category = modelMapper.map(categoryDto, Category.class);
		Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
		if (categoryFromDb != null) {
			throw new APIException("Category wit the name " + category.getCategoryName() + " is already exists");
		}
		Category savedCategory = categoryRepository.save(category);
		return modelMapper.map(savedCategory, CategoryDto.class);

	}

	@Override
	public String deleteCategory(Long categoryId) {
		/*
		 * Category category = categories.stream().filter(c ->
		 * c.getCategoryId().equals(categoryId)).findFirst() .orElseThrow(() -> new
		 * ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
		 * 
		 * categories.remove(category); return "category with categoryId : " +
		 * categoryId + " is deleted";
		 */
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		categoryRepository.delete(category);
		return "Category with the categoryId " + categoryId + " is deleted successfully";
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
		/*
		 * Optional<Category> optCategory = categories.stream().filter(c ->
		 * c.getCategoryId().equals(categoryId)) .findFirst(); if
		 * (optCategory.isPresent()) { Category existingCategory = optCategory.get();
		 * existingCategory.setCategoryName(category.getCategoryName()); return
		 * existingCategory; } else { throw new
		 * ResponseStatusException(HttpStatus.NOT_FOUND, "Category noy found"); }
		 * 
		 */
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		category.setCategoryName(categoryDto.getCategoryName());
		Category updatedCategory = categoryRepository.save(category);
		CategoryDto dto=modelMapper.map(updatedCategory,CategoryDto.class);
		return dto; 
	}

}
