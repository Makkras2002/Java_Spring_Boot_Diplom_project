package com.makkras.shop.scheduler.impl;

import com.makkras.shop.entity.Expenses;
import com.makkras.shop.entity.PeriodType;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.scheduler.CustomScheduler;
import com.makkras.shop.service.CurrentFinancesService;
import com.makkras.shop.service.ExpensesService;
import com.makkras.shop.service.impl.CustomCurrentFinancesService;
import com.makkras.shop.service.impl.CustomExpensesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class YearlyScheduler implements CustomScheduler {
    private static final String YEARLY_PERIOD = "0 0 9 1 1 ?";
    private static final Logger logger = LogManager.getLogger();
    private final CurrentFinancesService financesService;
    private final ExpensesService expensesService;

    @Autowired
    public YearlyScheduler(CustomCurrentFinancesService financesService, CustomExpensesService expensesService) {
        this.financesService = financesService;
        this.expensesService = expensesService;
    }
    @Override
    @Scheduled(cron = YEARLY_PERIOD)
    public void performFinancesRedactionBasedOnExpensesTask() {
        List<Expenses> expenses = expensesService.getAllExpensesByPeriod(PeriodType.YEARLY);
        expenses.forEach(expensesData -> {
            try {
                financesService.updateCurrentFinances(BigDecimal.ZERO.subtract(expensesData.getExpensesAmount()));
            } catch (CustomServiceException e) {
                logger.error(e.getMessage());
            }
        });
    }
}
