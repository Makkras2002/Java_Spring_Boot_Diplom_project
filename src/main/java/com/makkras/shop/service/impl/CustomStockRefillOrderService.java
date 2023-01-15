package com.makkras.shop.service.impl;

import com.makkras.shop.entity.*;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.repo.StockRefillOrdersJpaRepository;
import com.makkras.shop.repo.projection_interface.MoneyByDateStatistics;
import com.makkras.shop.repo.projection_interface.ProductsSellingStatistics;
import com.makkras.shop.service.StockRefillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomStockRefillOrderService implements StockRefillOrderService {
    private final StockRefillOrdersJpaRepository stockRefillOrdersJpaRepository;

    @Autowired
    public CustomStockRefillOrderService(StockRefillOrdersJpaRepository stockRefillOrdersJpaRepository) {
        this.stockRefillOrdersJpaRepository = stockRefillOrdersJpaRepository;
    }

    @Override
    public List<CompleteStockRefillOrder> getAllStockRefillOrders() {
        return stockRefillOrdersJpaRepository.findAll();
    }

    @Override
    public void addStockRefillOrder(List<ComponentStockRefillOrder> componentStockRefillOrdersList,
                                    User user, SupplierCompany supplierCompany) {
        stockRefillOrdersJpaRepository.save(new CompleteStockRefillOrder(user, false,
                LocalDate.now(), supplierCompany,componentStockRefillOrdersList));
    }

    @Override
    public void updateStockRefillOrderStatus(boolean newStatus,Long orderId) {
        stockRefillOrdersJpaRepository.updateStockRefillOrderStatus(newStatus,orderId);
    }

    @Override
    public CompleteStockRefillOrder getStockRefillOrderById(Long orderId) throws CustomServiceException {
        return stockRefillOrdersJpaRepository.findById(orderId).orElseThrow(CustomServiceException::new);
    }

    @Override
    public List<CompleteStockRefillOrder> getAllStockRefillOrdersByCompletionStatus(boolean completionStatus) {
        return stockRefillOrdersJpaRepository.findAllByIsCompleted(completionStatus);
    }

    @Override
    public List<CompleteStockRefillOrder> getAllStockRefillOrdersAndOrderByDateDesc() {
        return stockRefillOrdersJpaRepository.findAllByOrderByCompleteStockRefillOrderDateDesc();
    }

    @Override
    public List<CompleteStockRefillOrder> getAllStockRefillOrdersAndOrderByDateAsc() {
        return stockRefillOrdersJpaRepository.findAllByOrderByCompleteStockRefillOrderDateAsc();
    }

    @Override
    public List<CompleteStockRefillOrder> getAllFilteredStockRefillOrders(String loginOrEmail, String supplierCompanyName, LocalDate startDate, LocalDate endDate) {
        return stockRefillOrdersJpaRepository.findAllByUser_LoginLikeOrUser_EmailLikeAndSupplierCompanyNameLikeAndCompleteStockRefillOrderDateIsBetween(loginOrEmail,loginOrEmail,supplierCompanyName,startDate,endDate);
    }

    @Override
    public List<MoneyByDateStatistics> countExpensesOnStockRefillByDateStatistics() {
        return stockRefillOrdersJpaRepository.countExpensesOnStockRefillByDateStatisticsAndOrderByCompleteStockRefillOrderDateAsc();
    }

    @Override
    public List<ProductsSellingStatistics> countProductsStockRefillSellingStatistics() {
        return stockRefillOrdersJpaRepository.countProductsStockRefillSellingStatistics();
    }
}
