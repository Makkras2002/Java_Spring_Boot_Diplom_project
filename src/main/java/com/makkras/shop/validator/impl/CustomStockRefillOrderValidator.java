package com.makkras.shop.validator.impl;

import com.makkras.shop.entity.ComponentStockRefillOrder;
import com.makkras.shop.entity.Product;
import com.makkras.shop.validator.StockRefillOrderValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomStockRefillOrderValidator implements StockRefillOrderValidator {
    private static final Long minAmount = 1L;
    private static final BigDecimal minPrice = BigDecimal.ZERO;
    private static final String AMOUNT_ERROR = "Количество товара не должно быть меньше единицы!";
    private static final String PRICE_ERROR = "Цена товара не может быть меньше нуля!";
    private static final String PRODUCT_ALREADY_EXISTS_ERROR = "Данный продукт уже есть в заказе!";

    @Override
    public boolean validateOrderData(List<ComponentStockRefillOrder> orders,
                                     Long amount, BigDecimal price,
                                     Product product, List<String> errorParams) {
        if(amount<minAmount) {
            errorParams.add(AMOUNT_ERROR);
        }
        if(price.compareTo(minPrice)<0) {
            errorParams.add(PRICE_ERROR);
        }
        if(orders.stream().anyMatch(order -> order.getProduct().equals(product))) {
            errorParams.add(PRODUCT_ALREADY_EXISTS_ERROR);
        }
        return errorParams.size() == 0;
    }
}
