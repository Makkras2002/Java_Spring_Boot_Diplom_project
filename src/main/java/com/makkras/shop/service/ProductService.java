package com.makkras.shop.service;

import com.makkras.shop.entity.Product;

import java.util.List;

public interface ProductService {
    void addProduct(Product product);
    List<Product> getAllAvailableAndInStockProducts();
}
