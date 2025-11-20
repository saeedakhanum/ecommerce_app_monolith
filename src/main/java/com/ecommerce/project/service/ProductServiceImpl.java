package com.ecommerce.project.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.exception.APIException;
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
public class ProductServiceImpl implements ProductService
{

    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Value("${project.images}")
    private String path;

    // add product
    @Override
    public ProductDto addProduct(ProductDto productDto, Long categoryId)
    {
        logger.info("entering the addProduct method");
        // check if the product is already present or not
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        logger.info("Fetched category {}", category);

        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();
        for (Product product : products)
        {
            if (product.getProductName().equals(productDto.getProductName()))
            {
                isProductNotPresent = false;
                break;
            }
        }
        if (isProductNotPresent)
        {
            Double specialPrice = productDto.getPrice() - ((productDto.getDiscount() * 0.01) * productDto.getPrice());
            Product product = modelMapper.map(productDto, Product.class);
            product.setCategory(category);
            product.setImage("default.png");
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            logger.info("product saved successfully {}", product);
            ProductDto dto = modelMapper.map(savedProduct, ProductDto.class);
            dto.setCategoryDto(modelMapper.map(category, CategoryDto.class));
            logger.info("Retured product dto is {}", dto);
            logger.info("Exiting the addProduct method");
            return dto;
        } else
        {
            throw new APIException("Product already exists");
        }
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
    // get all products
    @Override
    public ProductResponse getAllProducts()
    {
        logger.info("Entering getAllProduct method");
        // check whether the list of the products is empty or not
        List<Product> products = productRepository.findAll();
        if (products.isEmpty())
        {
            throw new APIException("No products found");
        }
        logger.info("Total products fetched : {}", products.size());
        List<ProductDto> dtos = products.stream().map(product -> {
            ProductDto productDto = modelMapper.map(product, ProductDto.class);
            if (product.getCategory() != null)
            {
                CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
                productDto.setCategoryDto(categoryDto);
            }
            return productDto;
        }).collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(dtos);
        logger.info("total products returned in product response :{}", dtos.size());
        logger.info("Exiting the getAllProducts method");
        return productResponse;
    }

    // get products by categoryId
    @Override
    public ProductResponse getProductByCategory(Long categoryId)
    {
        logger.info("start of getProductByCategory method");
        // check whether the list of the products is empty or not
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        if (products.isEmpty())
        {
            throw new APIException("No products found");
        }
        logger.info("{} products are found with the categoryId {}", products.size(), categoryId);
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(products.stream().map(product -> {
            ProductDto productDto = modelMapper.map(product, ProductDto.class);
            if (product.getCategory() != null)
            {
                CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
                productDto.setCategoryDto(categoryDto);
            }
            return productDto;
        }).toList());
        logger.info("End of getProductByCategoryId method");
        return productResponse;
    }

    // search the product
    public ProductResponse searchProductByKeyword(String keyword)
    {
        logger.info("entering searchProductByKeyword method");
        // check whether the list of the products is empty or not
        logger.info("Fetching the products with the keyword : {}", keyword);
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase(keyword);
        if (products.isEmpty())
        {
            throw new APIException("No products found");
        }
        logger.debug("product fetched with the keyword : {}, count : {}", keyword, products.size());
        List<ProductDto> dtos = products.stream().map(product -> {
            ProductDto productDto = modelMapper.map(product, ProductDto.class);
            if (product.getCategory() != null)
            {
                CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
                productDto.setCategoryDto(categoryDto);
            }
            return productDto;

        }).toList();
        if (dtos.isEmpty())
        {
            logger.warn("No product found with the keyword : {}", keyword);
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(dtos);
        logger.info("exiting the getProductByKeyword with the count : {}", products.size());
        return productResponse;
    }

    // update the product
    @Override
    public ProductDto updateProduct(ProductDto productDto, Long categoryId, Long productId)
    {
        logger.info("entering the updateProduct method");
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        logger.info("Existing product found {} ", existingProduct);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categry", "categoryId", categoryId));
        logger.debug("category is {}", category);
        Double specialPrice = (productDto.getPrice()) - ((productDto.getDiscount() * 0.01) * productDto.getPrice());
        logger.debug("product after mapping DTO {}", existingProduct);
        Product product = modelMapper.map(productDto, Product.class);
        product.setProductId(existingProduct.getProductId());
        product.setCategory(category);
        product.setSpecialPrice(specialPrice);
        product.setImage(existingProduct.getImage());
        Product updatedProduct = productRepository.save(product);
        logger.info("product is updated with the productId {}", product.getProductId());
        ProductDto updatedProductDto = modelMapper.map(updatedProduct, ProductDto.class);
        if (product.getCategory() != null)
        {
            CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
            updatedProductDto.setCategoryDto(categoryDto);
        }
        return updatedProductDto;

    }

    @Override
    public ProductDto getProductByProductId(Long productId)
    {
        logger.info("Start of getProductByProductId method");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        logger.info("product fetched from db {}", product);
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        if (product.getCategory() != null)
        {
            CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
            productDto.setCategoryDto(categoryDto);
        }
        logger.info("Returned dto : {}", productDto);
        logger.info("End of getProductByProductId");
        return productDto;
    }

    public void deleteProduct(Long productId)
    {
        logger.info("Start of deleteProduct method");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        logger.info("fetching the product {} from db", product);
        productRepository.delete(product);
        logger.info("product is deleted successfully with the productId : {}", productId);
        logger.info("End of the deleteProduct method");
    }

    // update product image
    @Override
    public ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException
    {
        logger.info("Start of updateProductImage method");
        // get the product from DB
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        logger.info("fetching the product {} ", product);

        // Get the file name of the uploaded image
        // upload the image to the server
        logger.info("Calling the uploadImage method");
        String fileName = uploadImage(path, image);

        logger.info("Getting the file name {}", fileName);

        // update the image to the product
        product.setImage(fileName);

        // save the product
        Product updatedProduct = productRepository.save(product);

        logger.info("Product is updated successfully with the image {}", updatedProduct);

        // return product dto
        ProductDto dto = modelMapper.map(updatedProduct, ProductDto.class);
        if (updatedProduct.getCategory() != null)
        {
            CategoryDto categoryDto = modelMapper.map(updatedProduct.getCategory(), CategoryDto.class);
            dto.setCategoryDto(categoryDto);
        }
        logger.info("Returning the product dto {}", dto);
        logger.info("End of updateProductImage method");
        return dto;
    }

    public String uploadImage(String path, MultipartFile file) throws IOException
    {
        logger.info("Start of uploadImage method");
        // get the file name of the current/original file
        String originalFileName = file.getOriginalFilename();// istockphoto.jpg
        logger.info("Original fileName {}", originalFileName);

        // Generate unique file name
        String randomId = UUID.randomUUID().toString(); // 11 .jpg
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        logger.info("FileName {}", fileName);
        String filePath = path + File.separator + fileName; // images\91130907-e935-4977-9129-52172e6cabe7.jpg
        logger.info("filePath {}", filePath);

        // check of the path exist and created
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdir();

        // upload the file to the server
        Files.copy(file.getInputStream(), Paths.get(filePath));
        logger.info("File is uploaded successfully in the server");

        // return the file name
        logger.info("Returning the fileName {}", fileName);
        logger.info("End of uploadImage method");
        return fileName;
    }

}
