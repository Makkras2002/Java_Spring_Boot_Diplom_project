package com.makkras.shop.service;

import com.makkras.shop.entity.CompleteClientsOrder;
import com.makkras.shop.entity.ComponentClientsOrder;
import com.makkras.shop.entity.User;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.repo.projection_interface.MoneyByDateStatistics;
import com.makkras.shop.repo.projection_interface.ProductsSellingStatistics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ClientOrderService {
    void addClientOrder(List<ComponentClientsOrder> componentClientsOrdersList,
                        User user, String deliveryAddress, BigDecimal deliveryPrice);
    List<CompleteClientsOrder> getAllClientOrders();
    void updateClientOrderStatus(boolean newStatus, Long orderId);
    CompleteClientsOrder getClientOrderById(Long orderId) throws CustomServiceException;
    List<CompleteClientsOrder> getAllCompletedClientsOrders();
    List<CompleteClientsOrder> getAllUncompletedClientsOrders();
    List<CompleteClientsOrder> getAllClientsOrdersAndOrderByDateDesc();
    List<CompleteClientsOrder> getAllClientsOrdersAndOrderByDateAsc();
    List<CompleteClientsOrder> getAllFilteredClientsOrders(String loginOrEmail, String deliveryAddress, LocalDate startDate, LocalDate endDate);
    List<ProductsSellingStatistics> countProductsSellingStatistics(LocalDate startDate, LocalDate endDate);
    List<MoneyByDateStatistics> countEarningsByDateStatistics(LocalDate startDate, LocalDate endDate);
}
