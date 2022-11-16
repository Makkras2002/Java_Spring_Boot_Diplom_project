package com.makkras.shop.service;

import com.makkras.shop.entity.Expenses;
import com.makkras.shop.entity.PeriodType;
import com.makkras.shop.exception.CustomServiceException;

import java.util.List;

public interface ExpensesService {
    List<Expenses> getAllExpenses();
    List<Expenses> getAllExpensesAndOrderByAmountAsc();
    List<Expenses> getAllExpensesAndOrderByAmountDesc();
    List<Expenses> getAllExpensesByPeriod(PeriodType periodType);
    void updateExpensesData(Long expensesId, Expenses expensesToUpdate) throws CustomServiceException;
    void deleteExpensesById(Long expensesId);
    void addExpenses(Expenses expenses);
}
