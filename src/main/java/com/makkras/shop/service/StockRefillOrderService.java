package com.makkras.shop.service;

import com.makkras.shop.entity.CompleteStockRefillOrder;
import com.makkras.shop.entity.ComponentStockRefillOrder;
import com.makkras.shop.entity.SupplierCompany;
import com.makkras.shop.entity.User;
import com.makkras.shop.exception.CustomServiceException;

import java.util.List;

public interface StockRefillOrderService {
    List<CompleteStockRefillOrder> getAllStockRefillOrders();
    void addStockRefillOrder(List<ComponentStockRefillOrder> componentStockRefillOrdersList,
                             User user, SupplierCompany supplierCompany);
    void updateStockRefillOrderStatus(boolean newStatus,Long orderId);
    CompleteStockRefillOrder getStockRefillOrderById(Long orderId) throws CustomServiceException;
}
