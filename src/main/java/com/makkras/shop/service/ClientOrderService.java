package com.makkras.shop.service;

import com.makkras.shop.entity.ComponentClientsOrder;
import com.makkras.shop.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface ClientOrderService {
    void addClientsOrder(List<ComponentClientsOrder> componentClientsOrdersList,
                         User user, String deliveryAddress, BigDecimal deliveryPrice);
}
