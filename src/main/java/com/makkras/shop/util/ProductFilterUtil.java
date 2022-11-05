package com.makkras.shop.util;

import com.makkras.shop.entity.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductFilterUtil {
    public List<Product> filter(List<Product> unFilteredProducts, String name, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        return  unFilteredProducts.stream()
                .filter(product -> name==null || (product.getProductName().toLowerCase().contains(name.toLowerCase()) && name.length()>2) || name.length()<=2)
                .filter(product -> category==null || product.getCategory().getCategory().equals(category) || category.equals("-"))
                .filter(product -> minPrice== null || product.getProductPrice().doubleValue()>=minPrice.doubleValue())
                .filter(product -> maxPrice==null || product.getProductPrice().doubleValue()<= maxPrice.doubleValue())
                .collect(Collectors.toList());
    }
}
