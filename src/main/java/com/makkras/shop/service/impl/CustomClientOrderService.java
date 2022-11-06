package com.makkras.shop.service.impl;

import com.makkras.shop.entity.CompleteClientsOrder;
import com.makkras.shop.entity.ComponentClientsOrder;
import com.makkras.shop.entity.User;
import com.makkras.shop.repo.ClientOrderJpaRepository;
import com.makkras.shop.service.ClientOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CustomClientOrderService implements ClientOrderService {
    private ClientOrderJpaRepository clientOrderJpaRepository;

    @Autowired
    public CustomClientOrderService(ClientOrderJpaRepository clientOrderJpaRepository) {
        this.clientOrderJpaRepository = clientOrderJpaRepository;
    }

    public void addClientsOrder(List<ComponentClientsOrder> componentClientsOrdersList,
                                User user, String deliveryAddress, BigDecimal deliveryPrice) {
        clientOrderJpaRepository.save(new CompleteClientsOrder(user,false, LocalDate.now(),componentClientsOrdersList,deliveryAddress,deliveryPrice));
    }
}
