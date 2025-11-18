package com.ecommerce.project.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
		Sort sortByAndDir=sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndDir);
		 Page<Category> categoryPage = categoryRepository.findAll(pageable);
		 List<Category> categories = categoryPage.getContent();
		if (categories.isEmpty())
			throw new APIException("No category created till now");
		List<CategoryDto> list = categories.stream().map(category -> modelMapper.map(category, CategoryDto.class))
				.toList();
		CategoryResponse categoryResponse = new CategoryResponse();
		categoryResponse.setContent(list);
		categoryResponse.setPageNumber(categoryPage.getNumber());
		categoryResponse.setPageSize(categoryPage.getSize());
		categoryResponse.setTotalPages(categoryPage.getTotalPages());
		categoryResponse.setTotalElements(categoryPage.getTotalElements());
		categoryResponse.setLastPage(categoryPage.isLast());
		categoryResponse.setPreviousPage(categoryPage.hasPrevious());
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
	public CategoryDto deleteCategory(Long categoryId) {
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
		return modelMapper.map(category, CategoryDto.class);
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

	@Override
	public CategoryDto getCatgeory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));
		return modelMapper.map(category, CategoryDto.class);
	}

}
