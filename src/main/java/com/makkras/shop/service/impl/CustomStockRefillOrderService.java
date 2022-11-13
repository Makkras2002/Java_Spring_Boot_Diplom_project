package com.makkras.shop.service.impl;

import com.makkras.shop.entity.CompleteStockRefillOrder;
import com.makkras.shop.entity.ComponentStockRefillOrder;
import com.makkras.shop.entity.SupplierCompany;
import com.makkras.shop.entity.User;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.repo.StockRefillOrdersJpaRepository;
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
}
