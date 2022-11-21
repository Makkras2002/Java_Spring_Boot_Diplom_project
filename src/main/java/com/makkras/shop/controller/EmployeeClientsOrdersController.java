package com.makkras.shop.controller;

import com.google.gson.Gson;
import com.makkras.shop.entity.CompleteClientsOrder;
import com.makkras.shop.entity.ComponentClientsOrder;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.service.ClientOrderService;
import com.makkras.shop.service.CurrentFinancesService;
import com.makkras.shop.service.MailService;
import com.makkras.shop.service.ProductService;
import com.makkras.shop.service.impl.CustomClientOrderService;
import com.makkras.shop.service.impl.CustomCurrentFinancesService;
import com.makkras.shop.service.impl.CustomMailService;
import com.makkras.shop.service.impl.CustomProductService;
import com.makkras.shop.util.ExcelExporter;
import com.makkras.shop.util.impl.ClientsOrdersDataExcelExporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class EmployeeClientsOrdersController {
    public static final String NONE_ORDERS_FOUND_SEARCH_ERROR = "Заказы, соответствующие параметрам поиска, не были найдены!";
    private static final String CL_ORDER_ALREADY_COMPLETED_ERROR = "Этот заказ уже подтверждён!";
    public static final String ORDER_COMPLETION_ERROR = "Произошла ошибка во время подтверждения заказа!";
    public static final String FILTER_TYPE_NOT_SELECTED = "Способ фильтрации не был выбран!";
    public static final String ONLY_COMPLETED_ORDERS = "onlyCompleted";
    public static final String ONLY_UNCOMPLETED_ORDERS = "onlyUncompleted";
    private static final Logger logger = LogManager.getLogger();
    private final ClientOrderService clientOrderService;
    private final ProductService productService;
    private final CurrentFinancesService financesService;
    private final MailService mailService;
    private final ExcelExporter excelExporter;
    private final Gson gson;

    @Autowired
    public EmployeeClientsOrdersController(CustomClientOrderService clientOrderService,
                                           CustomProductService productService,
                                           CustomCurrentFinancesService financesService,
                                           CustomMailService mailService,
                                           ClientsOrdersDataExcelExporter excelExporter) {
        this.clientOrderService = clientOrderService;
        this.productService = productService;
        this.financesService = financesService;
        this.mailService = mailService;
        this.excelExporter = excelExporter;
        gson = new Gson();
    }

    @GetMapping("/clientsOrders")
    public String showClientsOrdersControlPage(Model model) {
        model.addAttribute("clientsOrders",gson.toJson(clientOrderService.getAllClientOrders()));
        return "clientsOrdersControlPage";
    }

    @PostMapping("/confirmClOrder")
    public String confirmClientOrder(@RequestParam Long order_id,
                                     @RequestParam boolean isCompleted,
                                     RedirectAttributes redirectAttributes) {
        if(!isCompleted) {
            try {
                clientOrderService.updateClientOrderStatus(true,order_id);
                CompleteClientsOrder clientsOrder = clientOrderService.getClientOrderById(order_id);
                BigDecimal totalSum = clientsOrder.getDeliveryPrice();
                for (ComponentClientsOrder componentOrder : clientsOrder.getClientsComponentOrders()) {
                    totalSum = totalSum.add(componentOrder.getOrderedProductFullPrice());
                    productService.updateProductAmountInStock(-componentOrder.getOrderedProductAmount(),
                            componentOrder.getProduct());
                }
                financesService.updateCurrentFinances(totalSum);
                try {
                    mailService.sendSuccessfulClientOrderEmail(clientsOrder.getUser(), Optional.ofNullable(clientsOrder.getDeliveryAddress()));
                } catch (MessagingException | UnsupportedEncodingException e) {
                    logger.error(e.getMessage());
                    redirectAttributes.addFlashAttribute("error", ORDER_COMPLETION_ERROR);
                }
            } catch (CustomServiceException exception) {
                logger.error(exception.getMessage());
                redirectAttributes.addFlashAttribute("error", ORDER_COMPLETION_ERROR);
            }
        } else {
            redirectAttributes.addFlashAttribute("error",CL_ORDER_ALREADY_COMPLETED_ERROR);
        }
        return "redirect:/clientsOrders";
    }

    @PostMapping("/filterClOrders")
    public String filterClientsOrders(Model model, @RequestParam(required = false) String filterFormSelect) {
        String filteredClientsOrdersInGson;
        if(filterFormSelect == null || filterFormSelect.equals("noneSelected")) {
            filteredClientsOrdersInGson = gson.toJson(clientOrderService.getAllClientOrders());
            model.addAttribute("error", FILTER_TYPE_NOT_SELECTED);

        } else if(filterFormSelect.equals("showCompleted")) {
            filteredClientsOrdersInGson = gson.toJson(clientOrderService.getAllCompletedClientsOrders());
        } else if(filterFormSelect.equals("showUncompleted")) {
            filteredClientsOrdersInGson = gson.toJson(clientOrderService.getAllUncompletedClientsOrders());
        } else if(filterFormSelect.equals("orderByDateDesc")) {
            filteredClientsOrdersInGson = gson.toJson(clientOrderService.getAllClientsOrdersAndOrderByDateDesc());
        }else if(filterFormSelect.equals("orderByDateAsc")) {
            filteredClientsOrdersInGson = gson.toJson(clientOrderService.getAllClientsOrdersAndOrderByDateAsc());
        }else {
            filteredClientsOrdersInGson = gson.toJson(clientOrderService.getAllClientOrders());
        }
        model.addAttribute("clientsOrders",filteredClientsOrdersInGson);
        return "clientsOrdersControlPage";
    }

    @PostMapping("/searchClOrders")
    public String searchClientsOrders(Model model,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String deliveryAddress,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                      @RequestParam String completionStatus) {
        if(startDate==null) {
            startDate = LocalDate.of(2000,1,1);
        }
        if(endDate == null) {
            endDate = LocalDate.now();
        }
        List<CompleteClientsOrder> filteredClientsOrders = clientOrderService.getAllFilteredClientsOrders("%"+name+"%","%"+deliveryAddress+"%",startDate,endDate);
        if(completionStatus.equals(ONLY_COMPLETED_ORDERS)) {
            filteredClientsOrders = filteredClientsOrders.stream().filter(CompleteClientsOrder::isCompleted).collect(Collectors.toList());
        } else if(completionStatus.equals(ONLY_UNCOMPLETED_ORDERS)) {
            filteredClientsOrders = filteredClientsOrders.stream().filter(order -> !order.isCompleted()).collect(Collectors.toList());
        }
        if(filteredClientsOrders.size() == 0) {
            model.addAttribute("error",NONE_ORDERS_FOUND_SEARCH_ERROR);
        }
        model.addAttribute("clientsOrders",gson.toJson(filteredClientsOrders));
        return "clientsOrdersControlPage";
    }

    @GetMapping("/clientsOrders/exportExcel")
    public void exportClOrdersData(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=clientsOrders_" + currentDateTime + ".xlsx";
            response.setHeader(headerKey, headerValue);
            excelExporter.export(response,clientOrderService.getAllClientsOrdersAndOrderByDateDesc());
        } catch (CustomServiceException e) {
            logger.error(e.getMessage());
        }
    }
}
