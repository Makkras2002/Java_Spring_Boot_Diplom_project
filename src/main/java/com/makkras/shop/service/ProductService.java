package com.makkras.shop.service;

import com.makkras.shop.entity.Product;
import com.makkras.shop.entity.ProductCategory;

import java.util.List;

public interface ProductService {
    void addProduct(Product product);
    List<Product> getAllAvailableAndInStockProducts();
    List<Product> getAllAvailableAndInStockProductsAndOrderByPriceAsc();
    List<Product> getAllAvailableAndInStockProductsAndOrderByPriceDesc();
    List<Product> getAllAvailableAndInStockProductsAndOrderByName();
    List<Product> getAllAvailableAndInStockProductsAndOrderByCategory();
    List<ProductCategory> getAllProductCategories();
}
