package com.ecommerce.project.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController
{

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    // add product
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto,
            @PathVariable Long categoryId)
    {

        return new ResponseEntity<ProductDto>(productService.addProduct(productDto, categoryId), HttpStatus.CREATED);

    }

    // get all products
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir)
    {
        logger.info("Recieved the request to add the product");
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    // get products by catgoryId
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir)
    {
        logger.info("Recieved request to find the product with the category id {}", categoryId);
        ProductResponse productResponse = productService.getProductByCategory(categoryId, pageNumber, pageSize, sortBy,
                sortDir);
        return ResponseEntity.ok(productResponse);

    }

    // search the product
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir)
    {
        logger.info("Recieved the request to search the product by the keyword " + keyword);
        return ResponseEntity
                .ok(productService.searchProductByKeyword("%" + keyword + "%", pageNumber, pageSize, sortBy, sortDir));

    }

    // update product
    @PutMapping("/admin/categories/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto,
            @PathVariable Long categoryId, @PathVariable(required = false) Long productId)
    {
        logger.info("Recieved request to update the product");
        return ResponseEntity.ok(productService.updateProduct(productDto, categoryId, productId));
    }

    // get product with the productId
    @GetMapping("/public/products/{productId}")
    public ResponseEntity<ProductDto> getProductByCategoryId(@PathVariable Long productId)
    {
        logger.info("Recieved the request to get the product with the productId {}", productId);
        return ResponseEntity.ok(productService.getProductByProductId(productId));
    }

    // delete product
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId)
    {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product is deleted successfully");
    }

    // update product image
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDto> updateProductImage(@PathVariable Long productId,
            @RequestParam("image") MultipartFile image) throws IOException
    {
        ProductDto updatedProductWithImage = productService.updateProductImage(productId, image);
        return new ResponseEntity<ProductDto>(updatedProductWithImage, HttpStatus.OK);
    }
}
