package com.makkras.shop.service;

import com.makkras.shop.entity.CurrentFinances;
import com.makkras.shop.exception.CustomServiceException;

import java.math.BigDecimal;

public interface CurrentFinancesService {
    void updateCurrentFinances(BigDecimal changeInFinances) throws CustomServiceException;
    CurrentFinances getCurrentFinances() throws CustomServiceException;
}
