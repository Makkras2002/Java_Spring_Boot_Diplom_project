package com.makkras.shop.validator.impl;

import com.makkras.shop.entity.Product;
import com.makkras.shop.validator.ProductDataValidator;
import org.springframework.stereotype.Service;

@Service
public class CustomProductDataValidator implements ProductDataValidator {
    private static final int MIN_NAME_AND_CATEGORY_AND_COMMENT_LENGTH = 3;
    private static final int MAX_NAME_AND_CATEGORY_LENGTH = 100;
    private static final int MAX_COMMENT_LENGTH = 500;
    private static final double MIN_PRICE_VALUE = 0;
    public boolean validateProductData(Product product) {
        return product.getProductName().length() >= MIN_NAME_AND_CATEGORY_AND_COMMENT_LENGTH &&
                product.getProductName().length() <= MAX_NAME_AND_CATEGORY_LENGTH &&
                product.getCategory().getCategory().length() >= MIN_NAME_AND_CATEGORY_AND_COMMENT_LENGTH &&
                product.getCategory().getCategory().length() <= MAX_NAME_AND_CATEGORY_LENGTH &&
                product.getProductComment().length() >= MIN_NAME_AND_CATEGORY_AND_COMMENT_LENGTH &&
                product.getProductComment().length() <= MAX_COMMENT_LENGTH &&
                !(product.getProductPrice().doubleValue() < MIN_PRICE_VALUE) &&
                (product.getPicturePath().endsWith(".png") || product.getPicturePath().isBlank());
    }
}
