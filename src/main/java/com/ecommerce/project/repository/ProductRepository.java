package com.ecommerce.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

	List<Product> findByCategoryOrderByPriceAsc(Category category);
	
	//select p from Product p where lower(p.productName) like (:keyword)
	List<Product> findByProductNameLikeIgnoreCase(String keyword);
}
