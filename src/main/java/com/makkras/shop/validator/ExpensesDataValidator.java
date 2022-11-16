package com.makkras.shop.validator;

import java.math.BigDecimal;

public interface ExpensesDataValidator {
    boolean validateExpensesData(String expensesName, BigDecimal expensesAmount, String period);
}
