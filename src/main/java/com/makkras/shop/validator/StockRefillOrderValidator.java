package com.makkras.shop.validator;

import com.makkras.shop.entity.ComponentStockRefillOrder;
import com.makkras.shop.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface StockRefillOrderValidator {
    boolean validateOrderData(List<ComponentStockRefillOrder> orders,
                              Long amount, BigDecimal price,
                              Product product, List<String> errorParams);
}
