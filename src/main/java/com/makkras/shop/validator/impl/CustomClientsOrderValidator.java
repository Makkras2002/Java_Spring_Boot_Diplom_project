package com.makkras.shop.validator.impl;

import com.makkras.shop.entity.ComponentClientsOrder;
import com.makkras.shop.entity.Product;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.service.ProductService;
import com.makkras.shop.service.impl.CustomProductService;
import com.makkras.shop.validator.ClientsOrderValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomClientsOrderValidator implements ClientsOrderValidator {
    private static final Logger logger = LogManager.getLogger();
    private final ProductService productService;


    @Autowired
    public CustomClientsOrderValidator(CustomProductService productService) {
        this.productService = productService;
    }

    public boolean validateClientOrder(List<ComponentClientsOrder> componentClientsOrdersList) {
        try {
            for(ComponentClientsOrder componentClientsOrder : componentClientsOrdersList) {
                Product product = productService.getProductById(componentClientsOrder.getProduct().getProductId());
                if(componentClientsOrder.getOrderedProductAmount() > product.getAmountInStock() || !product.isAvailable()) {
                    return false;
                }
            }
        } catch (CustomServiceException e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }
}
