package com.makkras.shop.service;

import com.makkras.shop.entity.*;
import com.makkras.shop.exception.CustomServiceException;

import java.time.LocalDate;
import java.util.List;

public interface StockRefillOrderService {
    List<CompleteStockRefillOrder> getAllStockRefillOrders();
    void addStockRefillOrder(List<ComponentStockRefillOrder> componentStockRefillOrdersList,
                             User user, SupplierCompany supplierCompany);
    void updateStockRefillOrderStatus(boolean newStatus,Long orderId);
    CompleteStockRefillOrder getStockRefillOrderById(Long orderId) throws CustomServiceException;
    List<CompleteStockRefillOrder> getAllStockRefillOrdersByCompletionStatus(boolean completionStatus);
    List<CompleteStockRefillOrder> getAllStockRefillOrdersAndOrderByDateDesc();
    List<CompleteStockRefillOrder> getAllStockRefillOrdersAndOrderByDateAsc();
    List<CompleteStockRefillOrder> getAllFilteredStockRefillOrders(String loginOrEmail, String supplierCompanyName, LocalDate startDate, LocalDate endDate);
}
