package com.ecommerce.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long productId;
	
	private String productName;
	private String image;
	private String description;
	private Integer quantity;
	private Double price; //100
	private Double discount; //25% =25         price-(discount/100*price)
	private Double specialPrice; //75
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name="seller_id")
	private User user;
	
	
}
