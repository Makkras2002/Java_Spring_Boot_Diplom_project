package com.makkras.shop.service.impl;

import com.makkras.shop.entity.Product;
import com.makkras.shop.entity.ProductCategory;
import com.makkras.shop.repo.CategoryJpaRepository;
import com.makkras.shop.repo.ProductJpaRepository;
import com.makkras.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomProductService implements ProductService {
    private final ProductJpaRepository productJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

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
    public List<Product> getAllProducts() {
        return productJpaRepository.findAll();
    }

    @Override
    public List<Product> getAllAvailableAndInStockProducts() {
        return productJpaRepository.findAllByAmountInStockGreaterThanAndIsAvailable(0L,true);
    }

    @Override
    public List<ProductCategory> getAllProductCategories() {
        return categoryJpaRepository.findAll();
    }

    @Override
    public List<Product> getAllAvailableAndInStockProductsAndOrderByPriceAsc() {
        return productJpaRepository.findAllByAmountInStockGreaterThanAndIsAvailableOrderByProductPriceAsc(0L,true);
    }

    @Override
    public List<Product> getAllAvailableAndInStockProductsAndOrderByPriceDesc() {
        return productJpaRepository.findAllByAmountInStockGreaterThanAndIsAvailableOrderByProductPriceDesc(0L,true);
    }

    @Override
    public List<Product> getAllAvailableAndInStockProductsAndOrderByName() {
        return productJpaRepository.findAllByAmountInStockGreaterThanAndIsAvailableOrderByProductName(0L,true);
    }

    @Override
    public List<Product> getAllAvailableAndInStockProductsAndOrderByCategory() {
        return productJpaRepository.findAllByAmountInStockGreaterThanAndIsAvailableOrderByCategoryCategory(0L,true);
    }

}
