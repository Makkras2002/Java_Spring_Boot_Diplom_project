package com.makkras.shop.service.impl;

import com.makkras.shop.entity.CurrentFinances;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.repo.FinancesJpaRepository;
import com.makkras.shop.service.CurrentFinancesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CustomCurrentFinancesService implements CurrentFinancesService {
    private static final Long CURRENT_FINANCES_ID = 0L;
    private final FinancesJpaRepository financesJpaRepository;

    @Autowired
    public CustomCurrentFinancesService(FinancesJpaRepository financesJpaRepository) {
        this.financesJpaRepository = financesJpaRepository;
    }

    public void updateCurrentFinances(BigDecimal changeInFinances) throws CustomServiceException {
        try {
            CurrentFinances oldFinances = financesJpaRepository.findById(CURRENT_FINANCES_ID).orElseThrow();
            financesJpaRepository.updateCurrentFinances(oldFinances.getFinancesAmount().add(changeInFinances),CURRENT_FINANCES_ID);
        } catch (NoSuchElementException exception) {
            throw new CustomServiceException(exception.getMessage());
        }

    }
}
