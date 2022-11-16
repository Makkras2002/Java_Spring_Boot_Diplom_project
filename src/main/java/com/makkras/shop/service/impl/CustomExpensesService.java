package com.makkras.shop.service.impl;

import com.makkras.shop.entity.Expenses;
import com.makkras.shop.entity.PeriodType;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.repo.ExpensesJpaRepository;
import com.makkras.shop.service.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomExpensesService implements ExpensesService {
    private final ExpensesJpaRepository expensesJpaRepository;

    @Autowired
    public CustomExpensesService(ExpensesJpaRepository expensesJpaRepository) {
        this.expensesJpaRepository = expensesJpaRepository;
    }

    @Override
    public List<Expenses> getAllExpenses() {
        return expensesJpaRepository.findAll();
    }

    @Override
    public void updateExpensesData(Long expensesId, Expenses expensesToUpdate) throws CustomServiceException {
        Expenses oldExpenses = expensesJpaRepository.findById(expensesId).orElseThrow(CustomServiceException::new);
        if(!expensesToUpdate.getExpensesName().equals(oldExpenses.getExpensesName())) {
            expensesJpaRepository.updateExpensesName(expensesToUpdate.getExpensesName(), expensesId);
        }
        if(expensesToUpdate.getExpensesAmount().compareTo(oldExpenses.getExpensesAmount()) != 0) {
            expensesJpaRepository.updateExpensesAmount(expensesToUpdate.getExpensesAmount(),expensesId);
        }
        if(!expensesToUpdate.getPeriod().getName().equals(oldExpenses.getPeriod().getName())) {
            expensesJpaRepository.updateExpensesPeriod(expensesToUpdate.getPeriod().getName(),expensesId);
        }
    }

    @Override
    public void deleteExpensesById(Long expensesId) {
        expensesJpaRepository.deleteById(expensesId);
    }

    @Override
    public void addExpenses(Expenses expenses) {
        expensesJpaRepository.save(expenses);
    }

    @Override
    public List<Expenses> getAllExpensesAndOrderByAmountAsc() {
        return expensesJpaRepository.findAllByOrderByExpensesAmountAsc();
    }

    @Override
    public List<Expenses> getAllExpensesAndOrderByAmountDesc() {
        return expensesJpaRepository.findAllByOrderByExpensesAmountDesc();
    }

    @Override
    public List<Expenses> getAllExpensesByPeriod(PeriodType periodType) {
        return expensesJpaRepository.findAllByPeriod(periodType);
    }
}
