package com.makkras.shop.service;

import com.makkras.shop.entity.CompleteClientsOrder;
import com.makkras.shop.entity.ComponentClientsOrder;
import com.makkras.shop.entity.User;
import com.makkras.shop.exception.CustomServiceException;

import java.math.BigDecimal;
import java.util.List;

public interface ClientOrderService {
    void addClientOrder(List<ComponentClientsOrder> componentClientsOrdersList,
                        User user, String deliveryAddress, BigDecimal deliveryPrice);
    List<CompleteClientsOrder> getAllClientOrders();
    void updateClientOrderStatus(boolean newStatus, Long orderId);
    CompleteClientsOrder getClientOrderById(Long orderId) throws CustomServiceException;
    List<CompleteClientsOrder> getAllCompletedClientsOrders();
    List<CompleteClientsOrder> getAllUncompletedClientsOrders();
}
