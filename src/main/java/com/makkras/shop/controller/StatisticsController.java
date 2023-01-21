package com.makkras.shop.controller;

import com.google.gson.Gson;
import com.makkras.shop.entity.Product;
import com.makkras.shop.repo.projection_interface.MoneyByDateStatistics;
import com.makkras.shop.repo.projection_interface.ProductsSellingStatistics;
import com.makkras.shop.service.ClientOrderService;
import com.makkras.shop.service.ProductService;
import com.makkras.shop.service.StockRefillOrderService;
import com.makkras.shop.service.impl.CustomClientOrderService;
import com.makkras.shop.service.impl.CustomProductService;
import com.makkras.shop.service.impl.CustomStockRefillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class StatisticsController {
    private final ClientOrderService clientOrderService;
    private final StockRefillOrderService stockRefillOrderService;
    private final ProductService productService;
    private final Gson gson;

    @Autowired
    public StatisticsController(CustomClientOrderService clientOrderService,
                                CustomStockRefillOrderService stockRefillOrderService,
                                CustomProductService productService) {
        this.clientOrderService = clientOrderService;
        this.stockRefillOrderService = stockRefillOrderService;
        this.productService = productService;
        gson = new Gson();
    }

    @RequestMapping(value = "/statistics", method = {RequestMethod.GET,RequestMethod.POST})
    public String showStatistics(Model model,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate) {
        startDate.ifPresentOrElse(localDate -> model.addAttribute("startDate", localDate),
                (Runnable) () -> {model.addAttribute("startDate", LocalDate.of(2022,1,1));});
        endDate.ifPresentOrElse(localDate -> model.addAttribute("endDate",localDate),
                (Runnable) () -> {model.addAttribute("endDate",LocalDate.now());});
        List<String> productNames = productService.getAllProducts().stream().map(Product::getProductName).collect(Collectors.toList());
        List<Long> productSoldAmount  = new ArrayList<>();
        List<Long> productBoughtAmount  = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        List<BigDecimal> earnedMoneyAmount  = new ArrayList<>();
        List<String> dates2 = new ArrayList<>();
        List<BigDecimal> spentMoneyAmount  = new ArrayList<>();
        List<ProductsSellingStatistics> sellingStatistics = clientOrderService
                .countProductsSellingStatistics(
                        (LocalDate) model.getAttribute("startDate"),
                        (LocalDate) model.getAttribute("endDate"));
        List<ProductsSellingStatistics> stockRefillSellingStatistics = stockRefillOrderService
                .countProductsStockRefillSellingStatistics(
                        (LocalDate) model.getAttribute("startDate"),
                        (LocalDate) model.getAttribute("endDate"));
        for(String productName : productNames) {
            productBoughtAmount
                    .add(stockRefillSellingStatistics
                            .stream()
                            .filter(stats -> stats.getName().equals(productName))
                            .findFirst().orElse(new ProductsSellingStatistics() {
                                @Override
                                public String getName() {
                                    return productName;
                                }

                                @Override
                                public Long getAmount() {
                                    return 0L;
                                }
                            }).getAmount());
            productSoldAmount
                    .add(sellingStatistics
                            .stream()
                            .filter(stats -> stats.getName().equals(productName))
                            .findFirst().orElse(new ProductsSellingStatistics() {
                                @Override
                                public String getName() {
                                    return productName;
                                }

                                @Override
                                public Long getAmount() {
                                    return 0L;
                                }
                            }).getAmount());
        }
        for (MoneyByDateStatistics earningsStatistics : clientOrderService
                .countEarningsByDateStatistics(
                (LocalDate) model.getAttribute("startDate"),
                (LocalDate) model.getAttribute("endDate"))) {
            dates.add(earningsStatistics.getDate().toString());
            earnedMoneyAmount.add(earningsStatistics.getAmount());
        }
        for(MoneyByDateStatistics expensesStatistics: stockRefillOrderService
                .countExpensesOnStockRefillByDateStatistics(
                        (LocalDate) model.getAttribute("startDate"),
                        (LocalDate) model.getAttribute("endDate"))) {
            dates2.add(expensesStatistics.getDate().toString());
            spentMoneyAmount.add(expensesStatistics.getAmount());
        }
        model.addAttribute("soldProductsNames",gson.toJson(productNames));
        model.addAttribute("soldProductsStats",gson.toJson(productSoldAmount));
        model.addAttribute("boughtProductsStats",gson.toJson(productBoughtAmount));
        model.addAttribute("dates",gson.toJson(dates));
        model.addAttribute("earnedMoneyAmount",gson.toJson(earnedMoneyAmount));
        model.addAttribute("dates2",gson.toJson(dates2));
        model.addAttribute("spentMoneyAmount",gson.toJson(spentMoneyAmount));
        return "statistics";
    }
}
