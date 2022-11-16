package com.makkras.shop.validator.impl;

import com.makkras.shop.entity.PeriodType;
import com.makkras.shop.validator.ExpensesDataValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class CustomExpensesDataValidator implements ExpensesDataValidator {
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 100;
    private static final BigDecimal MIN_EXPENSES_AMOUNT = BigDecimal.ZERO;
    @Override
    public boolean validateExpensesData(String expensesName, BigDecimal expensesAmount, String period) {
        return expensesName.length()>=MIN_NAME_LENGTH &&
                expensesName.length()<=MAX_NAME_LENGTH &&
                expensesAmount.compareTo(MIN_EXPENSES_AMOUNT)>=0 &&
                Arrays.stream(PeriodType.values()).anyMatch(periodType -> periodType.getName().equals(period));
    }
}
