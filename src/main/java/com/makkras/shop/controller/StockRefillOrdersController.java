package com.makkras.shop.controller;

import com.google.gson.Gson;
import com.makkras.shop.entity.CompleteStockRefillOrder;
import com.makkras.shop.entity.ComponentStockRefillOrder;
import com.makkras.shop.entity.Product;
import com.makkras.shop.entity.User;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.security.SecurityUser;
import com.makkras.shop.service.*;
import com.makkras.shop.service.impl.*;
import com.makkras.shop.validator.StockRefillOrderValidator;
import com.makkras.shop.validator.impl.CustomStockRefillOrderValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class StockRefillOrdersController {
    private static final String COMPONENT_STOCK_REFILL_ORDERS_LIST = "stockRefillOrders";
    private static final String NO_PRODUCTS_IN_ORDER_ERROR = "Ваш заказ пуст или не содержит такого товара!";
    private static final String EMPTY_ORDER_ERROR = "Заказ на пополнение склада пуст!";
    private static final String NO_PRODUCT_SELECTED_ERROR = "Продукт для добавления в заказ не был выбран!";
    private static final String CREATE_ORDER_ERROR = "Произошла ошибка во время создания заказа!";
    private static final String ORDER_ALREADY_COMPLETED_ERROR = "Заказ уже подтверждён!";
    private static final Logger logger = LogManager.getLogger();
    private final StockRefillOrderService stockRefillOrderService;
    private final ProductService productService;
    private final SupplierService supplierService;
    private final UserService userService;
    private final CurrentFinancesService financesService;
    private final StockRefillOrderValidator stockRefillOrderValidator;
    private final Gson gson;

    @Autowired
    public StockRefillOrdersController(CustomStockRefillOrderService stockRefillOrderService,
                                       CustomProductService productService,
                                       CustomStockRefillOrderValidator stockRefillOrderValidator,
                                       CustomSupplierService supplierService,
                                       CustomUserService userService, CustomCurrentFinancesService financesService) {
        this.stockRefillOrderService = stockRefillOrderService;
        this.productService = productService;
        this.stockRefillOrderValidator = stockRefillOrderValidator;
        this.supplierService = supplierService;
        this.userService = userService;
        this.financesService = financesService;
        gson = new Gson();
    }
    @GetMapping("/stockRefillOrders")
    public String showStockRefillOrders(Model model, HttpServletRequest request) {
        model.addAttribute("resupplyOrders",gson.toJson(stockRefillOrderService.getAllStockRefillOrders()));
        model.addAttribute("products",productService.getAllProducts());
        model.addAttribute("supplierCompanies",supplierService.getAllSupplierCompaniesWithActivityStatus(true));
        HttpSession session = request.getSession();
        List<ComponentStockRefillOrder> componentStockRefillOrdersList = (List<ComponentStockRefillOrder>) session.getAttribute(COMPONENT_STOCK_REFILL_ORDERS_LIST);
        if(componentStockRefillOrdersList == null) {
            componentStockRefillOrdersList = new ArrayList<>();
            session.setAttribute(COMPONENT_STOCK_REFILL_ORDERS_LIST,componentStockRefillOrdersList);
        }
        return "stockRefillOrders";
    }

    @PostMapping("/addProductToStockRefillOrder")
    public String addProductToStockRefillOrder(Model model,
                                               @RequestParam(required = false) Long productId,
                                               @RequestParam Long amount,
                                               @RequestParam BigDecimal fullPrice,
                                               HttpServletRequest request,
                                               RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        List<ComponentStockRefillOrder> componentStockRefillOrdersList = (List<ComponentStockRefillOrder>) session.getAttribute(COMPONENT_STOCK_REFILL_ORDERS_LIST);
        if(componentStockRefillOrdersList == null) {
            componentStockRefillOrdersList = new ArrayList<>();
            session.setAttribute(COMPONENT_STOCK_REFILL_ORDERS_LIST,componentStockRefillOrdersList);
        }
        try {
            if(productId == null) {
                redirectAttributes.addFlashAttribute("error",NO_PRODUCT_SELECTED_ERROR);
            } else {
                Product productToAdd = productService.getProductById(productId);
                List<String> errorParams = new ArrayList<>();
                if(stockRefillOrderValidator.validateOrderData(componentStockRefillOrdersList,amount,fullPrice,productToAdd,errorParams)) {
                    componentStockRefillOrdersList.add(new ComponentStockRefillOrder(productToAdd,amount,fullPrice));
                } else {
                    redirectAttributes.addFlashAttribute("error",errorParams);
                }
            }
        } catch (CustomServiceException e) {
            logger.error(e.getMessage());
        }
        return "redirect:/stockRefillOrders";
    }

    @PostMapping("/removeProductFromStockRefillOrder")
    public String removeProductFromStockRefillOrder(Model model, @RequestParam Long productId,
                                                    HttpServletRequest request,
                                                    RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        List<ComponentStockRefillOrder> componentStockRefillOrdersList = (List<ComponentStockRefillOrder>) session.getAttribute(COMPONENT_STOCK_REFILL_ORDERS_LIST);
        if(componentStockRefillOrdersList == null || componentStockRefillOrdersList.stream().noneMatch(order -> order.getProduct().getProductId().equals(productId))) {
            redirectAttributes.addFlashAttribute("error",NO_PRODUCTS_IN_ORDER_ERROR);
        } else {
            componentStockRefillOrdersList.remove(componentStockRefillOrdersList.stream().filter(order -> order.getProduct().getProductId().equals(productId)).findFirst().get());
        }
        return "redirect:/stockRefillOrders";
    }

    @PostMapping("/addSROrder")
    public String addSROrder(Model model,
                             @RequestParam Long supplierCompanyId,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request, Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Optional<User> user = userService.findActiveUserByLogin(securityUser.getUsername());
        if(user.isPresent()) {
            HttpSession session = request.getSession();
            List<ComponentStockRefillOrder> componentStockRefillOrdersList = (List<ComponentStockRefillOrder>) session.getAttribute(COMPONENT_STOCK_REFILL_ORDERS_LIST);
            if(componentStockRefillOrdersList == null || componentStockRefillOrdersList.isEmpty()) {
                redirectAttributes.addFlashAttribute("error",EMPTY_ORDER_ERROR);
            } else {
                try {
                    stockRefillOrderService.addStockRefillOrder(componentStockRefillOrdersList,user.get(),
                            supplierService.getSupplierCompanyById(supplierCompanyId));
                    session.removeAttribute(COMPONENT_STOCK_REFILL_ORDERS_LIST);
                } catch (CustomServiceException e) {
                    logger.error(e.getMessage());
                    redirectAttributes.addFlashAttribute("error",CREATE_ORDER_ERROR);
                }
            }
        } else {
            redirectAttributes.addFlashAttribute("error",ClientCatalogController.UNKNOWN_USER_ERROR);
        }
        return "redirect:/stockRefillOrders";
    }

    @PostMapping("/completeSROrder")
    public String completeStockRefillOrder(Model model,
                                           @RequestParam Long order_id,
                                           @RequestParam boolean isCompleted,
                                           RedirectAttributes redirectAttributes) {
        if(!isCompleted) {
            try {
                stockRefillOrderService.updateStockRefillOrderStatus(true,order_id);
                CompleteStockRefillOrder completeStockRefillOrder = stockRefillOrderService.getStockRefillOrderById(order_id);
                BigDecimal totalSum = BigDecimal.ZERO;
                for (ComponentStockRefillOrder componentOrder : completeStockRefillOrder.getStockRefillComponentOrders()) {
                    totalSum = totalSum.subtract(componentOrder.getOrderedProductFullPrice());
                    productService.updateProductAmountInStock(componentOrder.getOrderedProductAmount(),
                            componentOrder.getProduct());
                }
                financesService.updateCurrentFinances(totalSum.setScale(2, RoundingMode.HALF_UP));
            } catch (CustomServiceException exception) {
                logger.error(exception.getMessage());
                redirectAttributes.addFlashAttribute("error",EmployeeClientsOrdersController.ORDER_COMPLETION_ERROR);
            }
        } else {
            redirectAttributes.addFlashAttribute("error",ORDER_ALREADY_COMPLETED_ERROR);
        }
        return "redirect:/stockRefillOrders";
    }

    @PostMapping("/filterSROrders")
    public String filterClientsOrders(Model model, @RequestParam(required = false) String filterFormSelect) {
        String filteredStockRefillOrdersInGson;
        if(filterFormSelect == null || filterFormSelect.equals("noneSelected")) {
            filteredStockRefillOrdersInGson = gson.toJson(stockRefillOrderService.getAllStockRefillOrders());
            model.addAttribute("error", EmployeeClientsOrdersController.FILTER_TYPE_NOT_SELECTED);

        } else if(filterFormSelect.equals("showCompleted")) {
            filteredStockRefillOrdersInGson = gson.toJson(stockRefillOrderService.getAllStockRefillOrdersByCompletionStatus(true));
        } else if(filterFormSelect.equals("showUncompleted")) {
            filteredStockRefillOrdersInGson = gson.toJson(stockRefillOrderService.getAllStockRefillOrdersByCompletionStatus(false));
        } else if(filterFormSelect.equals("orderByDateDesc")) {
            filteredStockRefillOrdersInGson = gson.toJson(stockRefillOrderService.getAllStockRefillOrdersAndOrderByDateDesc());
        }else if(filterFormSelect.equals("orderByDateAsc")) {
            filteredStockRefillOrdersInGson = gson.toJson(stockRefillOrderService.getAllStockRefillOrdersAndOrderByDateAsc());
        }else {
            filteredStockRefillOrdersInGson = gson.toJson(stockRefillOrderService.getAllStockRefillOrders());
        }
        model.addAttribute("resupplyOrders",filteredStockRefillOrdersInGson);
        model.addAttribute("products",productService.getAllProducts());
        model.addAttribute("supplierCompanies",supplierService.getAllSupplierCompaniesWithActivityStatus(true));
        return "stockRefillOrders";
    }

    @PostMapping("/searchSROrders")
    public String searchClientsOrders(Model model,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String supplierCompanyName,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                      @RequestParam String completionStatus) {
        if(supplierCompanyName == null) {
            supplierCompanyName = "";
        }
        if(startDate==null) {
            startDate = LocalDate.of(2000,1,1);
        }
        if(endDate == null) {
            endDate = LocalDate.now();
        }
        List<CompleteStockRefillOrder> filteredOrders = stockRefillOrderService.getAllFilteredStockRefillOrders("%"+name+"%","%"+supplierCompanyName+"%",startDate,endDate);
        if(completionStatus.equals(EmployeeClientsOrdersController.ONLY_COMPLETED_ORDERS)) {
            filteredOrders = filteredOrders.stream().filter(CompleteStockRefillOrder::isCompleted).collect(Collectors.toList());
        } else if(completionStatus.equals(EmployeeClientsOrdersController.ONLY_UNCOMPLETED_ORDERS)) {
            filteredOrders = filteredOrders.stream().filter(order -> !order.isCompleted()).collect(Collectors.toList());
        }
        if(filteredOrders.size() == 0) {
            model.addAttribute("error",EmployeeClientsOrdersController.NONE_ORDERS_FOUND_SEARCH_ERROR);
        }
        model.addAttribute("resupplyOrders",gson.toJson(filteredOrders));
        model.addAttribute("products",productService.getAllProducts());
        model.addAttribute("supplierCompanies",supplierService.getAllSupplierCompaniesWithActivityStatus(true));
        return "stockRefillOrders";
    }
}
