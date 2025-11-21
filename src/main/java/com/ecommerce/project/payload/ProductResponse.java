package com.ecommerce.project.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
	private List<ProductDto> content;
	private Integer pageNumber;
	private Integer pageSize;
	private Long totalElements;
	private Integer totalPages;
	private boolean lastPage;
	private boolean previous;
}
