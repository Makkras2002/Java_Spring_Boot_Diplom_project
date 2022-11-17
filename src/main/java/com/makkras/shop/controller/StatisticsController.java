package com.makkras.shop.controller;

import com.google.gson.Gson;
import com.makkras.shop.repo.projection_interface.MoneyByDateStatistics;
import com.makkras.shop.repo.projection_interface.ProductsSellingStatistics;
import com.makkras.shop.service.ClientOrderService;
import com.makkras.shop.service.StockRefillOrderService;
import com.makkras.shop.service.impl.CustomClientOrderService;
import com.makkras.shop.service.impl.CustomStockRefillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StatisticsController {
    private final ClientOrderService clientOrderService;
    private final StockRefillOrderService stockRefillOrderService;
    private final Gson gson;

    @Autowired
    public StatisticsController(CustomClientOrderService clientOrderService,
                                CustomStockRefillOrderService stockRefillOrderService) {
        this.clientOrderService = clientOrderService;
        this.stockRefillOrderService = stockRefillOrderService;
        gson = new Gson();
    }

    @GetMapping("/statistics")
    public String showStatistics(Model model) {
        List<String> productNames = new ArrayList<>();
        List<Long> productSoldAmount  = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        List<BigDecimal> earnedMoneyAmount  = new ArrayList<>();
        List<String> dates2 = new ArrayList<>();
        List<BigDecimal> spentMoneyAmount  = new ArrayList<>();
        for(ProductsSellingStatistics sellingStatistics : clientOrderService.countProductsSellingStatistics()) {
            productNames.add(sellingStatistics.getName());
            productSoldAmount.add(sellingStatistics.getAmount());
        }
        for (MoneyByDateStatistics earningsStatistics : clientOrderService.countEarningsByDateStatistics()) {
            dates.add(earningsStatistics.getDate().toString());
            earnedMoneyAmount.add(earningsStatistics.getAmount());
        }
        for(MoneyByDateStatistics expensesStatistics: stockRefillOrderService.countExpensesOnStockRefillByDateStatistics()) {
            dates2.add(expensesStatistics.getDate().toString());
            spentMoneyAmount.add(expensesStatistics.getAmount());
        }
        model.addAttribute("soldProductsNames",gson.toJson(productNames));
        model.addAttribute("soldProductsStats",gson.toJson(productSoldAmount));
        model.addAttribute("dates",gson.toJson(dates));
        model.addAttribute("earnedMoneyAmount",gson.toJson(earnedMoneyAmount));
        model.addAttribute("dates2",gson.toJson(dates2));
        model.addAttribute("spentMoneyAmount",gson.toJson(spentMoneyAmount));
        return "statistics";
    }
}
