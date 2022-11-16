package com.makkras.shop.repo;

import com.makkras.shop.entity.Expenses;
import com.makkras.shop.entity.PeriodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ExpensesJpaRepository extends JpaRepository<Expenses, Long> {

    List<Expenses> findAllByOrderByExpensesAmountAsc();
    List<Expenses> findAllByOrderByExpensesAmountDesc();
    List<Expenses> findAllByPeriod(PeriodType periodType);

    @Modifying
    @Transactional
    @Query(value = "UPDATE expenses SET expenses_name = ? WHERE expenses_id = ?",nativeQuery = true)
    void updateExpensesName(String newName, Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE expenses SET expenses_amount = ? WHERE expenses_id = ?",nativeQuery = true)
    void updateExpensesAmount(BigDecimal newAmount, Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE expenses SET expenses_period = ? WHERE expenses_id = ?",nativeQuery = true)
    void updateExpensesPeriod(String period, Long id);
}
