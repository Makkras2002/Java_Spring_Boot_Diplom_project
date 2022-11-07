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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Optional;

@Controller
public class EmployeeClientsOrdersController {
    private static final String CL_ORDER_ALREADY_COMPLETED_ERROR = "Этот заказ уже подтверждён!";
    private static final String CL_ORDER_COMPLETION_ERROR = "Произошла ошибка во время подтверждения заказа!";
    private static final String FILTER_TYPE_NOT_SELECTED = "Способ фильтрации не был выбран!";
    private static final Logger logger = LogManager.getLogger();
    private final ClientOrderService clientOrderService;
    private final ProductService productService;
    private final CurrentFinancesService financesService;
    private final MailService mailService;
    private final Gson gson;

    @Autowired
    public EmployeeClientsOrdersController(CustomClientOrderService clientOrderService,
                                           CustomProductService productService,
                                           CustomCurrentFinancesService financesService,
                                           CustomMailService mailService) {
        this.clientOrderService = clientOrderService;
        this.productService = productService;
        this.financesService = financesService;
        this.mailService = mailService;
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
                    redirectAttributes.addFlashAttribute("error",CL_ORDER_COMPLETION_ERROR);
                }
            } catch (CustomServiceException exception) {
                logger.error(exception.getMessage());
                redirectAttributes.addFlashAttribute("error",CL_ORDER_COMPLETION_ERROR);
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
        } else {
            filteredClientsOrdersInGson = gson.toJson(clientOrderService.getAllClientOrders());
        }
        model.addAttribute("clientsOrders",filteredClientsOrdersInGson);
        return "clientsOrdersControlPage";
    }
}
