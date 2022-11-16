package com.makkras.shop.controller;

import com.google.gson.Gson;
import com.makkras.shop.entity.Expenses;
import com.makkras.shop.entity.PeriodType;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.service.CurrentFinancesService;
import com.makkras.shop.service.ExpensesService;
import com.makkras.shop.service.impl.CustomCurrentFinancesService;
import com.makkras.shop.service.impl.CustomExpensesService;
import com.makkras.shop.validator.ExpensesDataValidator;
import com.makkras.shop.validator.impl.CustomExpensesDataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Locale;

@Controller
public class FinancesController {
    private static final String FINANCES_EXTRACTION_ERROR = "Произошла ошибка во время чтения данных о финансах предприятия!";
    private static final String EXPENSES_DATA_VALIDATION_ERROR = "Введены неверные данные!";
    private static final String EXPENSES_DATA_UPDATE_ERROR = "Произошла ошибка во время обновления данных о расходах!";
    private static final String FINANCES_DATA_UPDATE_ERROR = "Произошла ошибка во время обновления данных о финансах!";
    private static final Logger logger = LogManager.getLogger();
    private final CurrentFinancesService financesService;
    private final ExpensesService expensesService;
    private final ExpensesDataValidator expensesDataValidator;
    private final Gson gson;

    @Autowired
    public FinancesController(CustomCurrentFinancesService financesService,
                              CustomExpensesService expensesService,
                              CustomExpensesDataValidator expensesDataValidator) {
        this.financesService = financesService;
        this.expensesService = expensesService;
        this.expensesDataValidator = expensesDataValidator;
        gson = new Gson();
    }

    @GetMapping("/finances")
    public String showFinances(Model model) {
        try {
            model.addAttribute("expenses",gson.toJson(expensesService.getAllExpenses()));
            model.addAttribute("periodTypes", PeriodType.values());
            model.addAttribute("currentFinances", financesService.getCurrentFinances());
        } catch (CustomServiceException e) {
            logger.error(e.getMessage());
            model.addAttribute("error",FINANCES_EXTRACTION_ERROR);
        }
        return "finances";
    }

    @PostMapping("/updateExpensesData")
    public String updateExpensesData(Model model,
                                     @RequestParam Long expenses_id,
                                     @RequestParam String name,
                                     @RequestParam BigDecimal expensesAmount,
                                     @RequestParam String period, RedirectAttributes redirectAttributes) {
        if(expensesDataValidator.validateExpensesData(name,expensesAmount,period)) {
            Expenses expensesToUpdate = new Expenses(expenses_id,name,expensesAmount,PeriodType.valueOf(period.toUpperCase(Locale.ROOT)));
            try {
                expensesService.updateExpensesData(expenses_id,expensesToUpdate);
            } catch (CustomServiceException e) {
                logger.error(e.getMessage());
                redirectAttributes.addFlashAttribute("error",EXPENSES_DATA_UPDATE_ERROR);
            }
        } else {
            redirectAttributes.addFlashAttribute("error",EXPENSES_DATA_VALIDATION_ERROR);
        }
        return "redirect:/finances";
    }

    @PostMapping("/deleteExpensesData")
    public String deleteExpensesData(Model model, @RequestParam Long expenses_id_for_delete) {
        expensesService.deleteExpensesById(expenses_id_for_delete);
        return "redirect:/finances";
    }

    @PostMapping("/addExpenses")
    public String addExpenses(Model model,
                              @RequestParam String name,
                              @RequestParam BigDecimal amount,
                              @RequestParam String period,
                              RedirectAttributes redirectAttributes) {
        if(expensesDataValidator.validateExpensesData(name,amount,period)) {
            expensesService.addExpenses(new Expenses(name,amount,PeriodType.valueOf(period)));
        } else {
            redirectAttributes.addFlashAttribute("error",EXPENSES_DATA_VALIDATION_ERROR);
        }
        return "redirect:/finances";
    }

    @PostMapping("/changeFinances")
    public String changeFinances(Model model,
                                 @RequestParam BigDecimal amount,
                                 RedirectAttributes redirectAttributes) {
        try {
            financesService.updateCurrentFinances(amount);
        } catch (CustomServiceException e) {
            logger.error(e.getMessage());
            redirectAttributes.addFlashAttribute("error",FINANCES_DATA_UPDATE_ERROR);
        }
        return "redirect:/finances";
    }

    @PostMapping("/sortExpenses")
    public String sortExpenses(Model model, @RequestParam(required = false) String sortFormSelect) {
        try {
            String sortedExpensesInGson;
            if(sortFormSelect == null || sortFormSelect.equals("noneSelected")) {
                sortedExpensesInGson = gson.toJson(expensesService.getAllExpenses());
                model.addAttribute("error",ClientCatalogController.SORT_TYPE_NOT_SELECTED_ERROR);

            } else if(sortFormSelect.equals("byPriceAsc")) {
                sortedExpensesInGson = gson.toJson(expensesService.getAllExpensesAndOrderByAmountAsc());

            } else if(sortFormSelect.equals("byPriceDesc")) {
                sortedExpensesInGson = gson.toJson(expensesService.getAllExpensesAndOrderByAmountDesc());
            }else {
                sortedExpensesInGson = gson.toJson(expensesService.getAllExpenses());
            }
            model.addAttribute("expenses",sortedExpensesInGson);
            model.addAttribute("periodTypes", PeriodType.values());
            model.addAttribute("currentFinances", financesService.getCurrentFinances());
        } catch (CustomServiceException e) {
            logger.error(e.getMessage());
            model.addAttribute("error",FINANCES_EXTRACTION_ERROR);
        }
        return "finances";
    }
}
