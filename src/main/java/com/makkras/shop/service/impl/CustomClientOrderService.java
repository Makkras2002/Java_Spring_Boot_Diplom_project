package com.makkras.shop.service.impl;

import com.makkras.shop.entity.CompleteClientsOrder;
import com.makkras.shop.entity.ComponentClientsOrder;
import com.makkras.shop.entity.User;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.repo.ClientOrderJpaRepository;
import com.makkras.shop.repo.projection_interface.MoneyByDateStatistics;
import com.makkras.shop.repo.projection_interface.ProductsSellingStatistics;
import com.makkras.shop.service.ClientOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomClientOrderService implements ClientOrderService {
    private final ClientOrderJpaRepository clientOrderJpaRepository;

    @Autowired
    public CustomClientOrderService(ClientOrderJpaRepository clientOrderJpaRepository) {
        this.clientOrderJpaRepository = clientOrderJpaRepository;
    }

    @Override
    public void addClientOrder(List<ComponentClientsOrder> componentClientsOrdersList,
                               User user, String deliveryAddress, BigDecimal deliveryPrice) {
        clientOrderJpaRepository.save(new CompleteClientsOrder(user,false, LocalDate.now(),componentClientsOrdersList,deliveryAddress,deliveryPrice));
    }

    @Override
    public List<CompleteClientsOrder> getAllClientOrders() {
        return clientOrderJpaRepository.findAll();
    }
    @Override
    public void updateClientOrderStatus(boolean newStatus, Long orderId) {
        clientOrderJpaRepository.updateClientOrderStatus(newStatus,orderId);
    }

    @Override
    public CompleteClientsOrder getClientOrderById(Long orderId) throws CustomServiceException {
        try {
            return clientOrderJpaRepository.findById(orderId).orElseThrow();
        } catch (NoSuchElementException exception) {
            throw new CustomServiceException(exception.getMessage());
        }
    }

    @Override
    public List<CompleteClientsOrder> getAllCompletedClientsOrders() {
        return clientOrderJpaRepository.findAllByIsCompleted(true);
    }
    @Override
    public List<CompleteClientsOrder> getAllUncompletedClientsOrders() {
        return clientOrderJpaRepository.findAllByIsCompleted(false);
    }

    @Override
    public List<CompleteClientsOrder> getAllClientsOrdersAndOrderByDateDesc() {
        return clientOrderJpaRepository.findAllByOrderByCompleteClientsOrderDateDesc();
    }

    @Override
    public List<CompleteClientsOrder> getAllClientsOrdersAndOrderByDateAsc() {
        return clientOrderJpaRepository.findAllByOrderByCompleteClientsOrderDateAsc();
    }

    @Override
    public List<CompleteClientsOrder> getAllFilteredClientsOrders(String loginOrEmail, String deliveryAddress, LocalDate startDate, LocalDate endDate) {
        return clientOrderJpaRepository.findAllByUser_LoginLikeOrUser_EmailLikeAndDeliveryAddressLikeAndCompleteClientsOrderDateIsBetween(loginOrEmail,loginOrEmail,deliveryAddress,startDate,endDate);
    }

    @Override
    public List<ProductsSellingStatistics> countProductsSellingStatistics() {
        return clientOrderJpaRepository.countProductsSellingStatistics();
    }

    @Override
    public List<MoneyByDateStatistics> countEarningsByDateStatistics() {
        return clientOrderJpaRepository.countEarningsByDateStatisticsAndOrderByCompleteClientsOrderDateAsc();
    }
}
