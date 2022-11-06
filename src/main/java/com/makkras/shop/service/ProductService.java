package com.makkras.shop.service;

import com.makkras.shop.entity.Product;
import com.makkras.shop.entity.ProductCategory;
import com.makkras.shop.exception.CustomServiceException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    void addProduct(Product product);
    List<Product> getAllAvailableAndInStockProducts();
    List<Product> getAllAvailableAndInStockProductsAndOrderByPriceAsc();
    List<Product> getAllAvailableAndInStockProductsAndOrderByPriceDesc();
    List<Product> getAllAvailableAndInStockProductsAndOrderByName();
    List<Product> getAllAvailableAndInStockProductsAndOrderByCategory();
    List<ProductCategory> getAllProductCategories();
    List<Product> getAllProducts();
    List<Product> getAllProductsAndOrderByPriceAsc();
    List<Product> getAllProductsAndOrderByPriceDesc();
    List<Product> getAllProductsAndOrderByName();
    List<Product> getAllProductsAndOrderByCategory();
    List<Product> getAllProductsAndOrderByIsAvailableDesc();
    List<Product> getAllProductsAndOrderByIsAvailableAsc();
    boolean updateProductData(Product updatedProduct, String productPictureLocationDir, MultipartFile pictureFile) throws CustomServiceException;
    Product getProductById(Long productId) throws CustomServiceException;
}
