package com.makkras.shop.validator;

import com.makkras.shop.entity.ComponentClientsOrder;

import java.util.List;

public interface ClientsOrderValidator {
    boolean validateClientOrder(List<ComponentClientsOrder> componentClientsOrdersList);
}
