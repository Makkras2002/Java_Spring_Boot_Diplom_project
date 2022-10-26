package com.makkras.shop.service.impl;

import com.makkras.shop.entity.Product;
import com.makkras.shop.repo.CategoryJpaRepository;
import com.makkras.shop.repo.ProductJpaRepository;
import com.makkras.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomProductService implements ProductService {
    private ProductJpaRepository productJpaRepository;
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    public CustomProductService(ProductJpaRepository productJpaRepository,
                                CategoryJpaRepository categoryJpaRepository) {
        this.productJpaRepository = productJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public void addProduct(Product product) {
        if(!categoryJpaRepository
                .existsByCategoryIdAndCategory(product.getCategory().getCategoryId(),
                        product.getCategory().getCategory())) {
            categoryJpaRepository.save(product.getCategory());
        }
        productJpaRepository.save(product);
    }

    @Override
    public List<Product> getAllAvailableAndInStockProducts() {
        return productJpaRepository.findAllByAmountInStockGreaterThanAndIsAvailable(0L,true);
    }

}
