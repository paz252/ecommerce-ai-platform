package com.paz252.ecommerce.product_service.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.paz252.ecommerce.product_service.dto.ProductRequest;
import com.paz252.ecommerce.product_service.dto.ProductResponse;
import com.paz252.ecommerce.product_service.model.Product;
import com.paz252.ecommerce.product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

// We use @Cacheable to read from Redis, @CacheEvict to flush stale cache entries, and explicitly manage custom cache mappings.

@Service
@RequiredArgsConstructor // Constructor Injection of dependency, better suited than Autowired (field level DI) 
public class ProductService {

    private final ProductRepository productRepository;

    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse createProduct(ProductRequest request){
        Product product = Product.builder()
                            .name(request.name())
                            .description(request.description())
                            .price(request.price())
                            .stockQuantity(request.stockQuantity())
                            .build();

        Product saveProduct = productRepository.save(product);
        return mapToResponse(saveProduct);
    }

    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(Long id){
        Product product = productRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        return mapToResponse(product);
    }

    @Cacheable(value = "products_all")
    public List<ProductResponse> getAllProducts(){
        return productRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    private ProductResponse mapToResponse(Product product){
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStockQuantity()
        );
    }

}
