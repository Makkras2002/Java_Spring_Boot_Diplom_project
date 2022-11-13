package com.makkras.shop.controller;

import com.google.gson.Gson;
import com.makkras.shop.entity.ComponentClientsOrder;
import com.makkras.shop.entity.Product;
import com.makkras.shop.entity.User;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.security.SecurityUser;
import com.makkras.shop.service.ClientOrderService;
import com.makkras.shop.service.ProductService;
import com.makkras.shop.service.UserService;
import com.makkras.shop.service.impl.CustomClientOrderService;
import com.makkras.shop.service.impl.CustomMailService;
import com.makkras.shop.service.impl.CustomProductService;
import com.makkras.shop.util.ProductFilterUtil;
import com.makkras.shop.validator.ClientsOrderValidator;
import com.makkras.shop.validator.impl.CustomClientsOrderValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ClientCatalogController {
    public static final String SORT_TYPE_NOT_SELECTED_ERROR = "Способ сортировки не был выбран!";
    public static final String NONE_PRODUCTS_WERE_FOUND_DURING_SEARCH = "Продукты, соответствующие параметрам поиска, не были найдены!";
    private static final String COMPONENT_ORDERS_LIST = "orders";
    private static final String EMPTY_ORDER_ERROR = "Ваш заказ пуст!";
    private static final String ORDER_ALTER_ERROR = "Возникла ошибка при изменении заказа!";
    private static final String ORDER_INVALID_PRODUCT_ERROR = "Ошибка при совершении заказа! Вы пытались заказать неверное количество или вид товара!";
    public static final String UNKNOWN_USER_ERROR = "Неизвестный пользователь!";
    private static final String NO_DELIVERY_ADDRESS = "None";
    private static final Logger logger = LogManager.getLogger();
    private final ProductService productService;
    private final UserService userService;
    private final ClientOrderService clientOrderService;
    private final ProductFilterUtil productFilterUtil;
    private final ClientsOrderValidator clientsOrderValidator;
    private final Gson gson;

    @Autowired
    public ClientCatalogController(CustomProductService productService,
                                   UserService userService,
                                   CustomClientOrderService clientOrderService,
                                   CustomMailService mailService,
                                   ProductFilterUtil productFilterUtil,
                                   CustomClientsOrderValidator clientsOrderValidator) {
        this.productService = productService;
        this.userService = userService;
        this.clientOrderService = clientOrderService;
        this.productFilterUtil = productFilterUtil;
        this.clientsOrderValidator =clientsOrderValidator;
        this.gson = new Gson();
    }

    @GetMapping("/catalog")
    public String showCatalog(Model model) {
        String productsInStockDataInGson = gson.toJson(productService.getAllAvailableAndInStockProducts());
        model.addAttribute("productsInStock", productsInStockDataInGson);
        model.addAttribute("categories",productService.getAllProductCategories());
        return "catalog";
    }

    @PostMapping("/search")
    public String searchAndFilterInCatalog(Model model,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) String category,
                                           @RequestParam(required = false) BigDecimal minPrice,
                                           @RequestParam(required = false) BigDecimal maxPrice) {
        List<Product> allProducts = productService.getAllAvailableAndInStockProducts();
        List<Product> filteredProducts = productFilterUtil.filter(allProducts,name,category,minPrice,maxPrice);
        if(filteredProducts.size()==0) {
            model.addAttribute("error",NONE_PRODUCTS_WERE_FOUND_DURING_SEARCH);
        }
        String filteredProductsDataInGson = gson.toJson(filteredProducts);
        model.addAttribute("productsInStock",filteredProductsDataInGson);
        model.addAttribute("categories",productService.getAllProductCategories());
        return "catalog";
    }

    @PostMapping("/sortCatalog")
    public String sortCatalog(Model model, @RequestParam(required = false) String sortFormSelect) {
        String sortedProductsInGson;
        if(sortFormSelect == null || sortFormSelect.equals("noneSelected")) {
            sortedProductsInGson = gson.toJson(productService.getAllAvailableAndInStockProducts());
            model.addAttribute("error",SORT_TYPE_NOT_SELECTED_ERROR);

        } else if(sortFormSelect.equals("byPriceAsc")) {
            sortedProductsInGson = gson.toJson(productService.getAllAvailableAndInStockProductsAndOrderByPriceAsc());
        } else if(sortFormSelect.equals("byPriceDesc")) {
            sortedProductsInGson = gson.toJson(productService.getAllAvailableAndInStockProductsAndOrderByPriceDesc());
        } else if(sortFormSelect.equals("byName")) {
            sortedProductsInGson = gson.toJson(productService.getAllAvailableAndInStockProductsAndOrderByName());
        } else {
            sortedProductsInGson = gson.toJson(productService.getAllAvailableAndInStockProductsAndOrderByCategory());
        }
        model.addAttribute("productsInStock",sortedProductsInGson);
        model.addAttribute("categories",productService.getAllProductCategories());
        return "catalog";
    }

    @PostMapping("/addToBasket")
    public String addToProductBasket(Model model,
                                     @RequestParam Long product_id,
                                     @RequestParam Long amount,
                                     RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            List<ComponentClientsOrder> componentClientsOrdersList = (List<ComponentClientsOrder>) session.getAttribute(COMPONENT_ORDERS_LIST);
            Product productToOrder = productService.getProductById(product_id);
            if(componentClientsOrdersList == null) {
                componentClientsOrdersList = new ArrayList<>();
                session.setAttribute(COMPONENT_ORDERS_LIST,componentClientsOrdersList);
            }
            Optional<ComponentClientsOrder> clientsOrderOptional = componentClientsOrdersList.stream().filter(componentClientsOrder -> componentClientsOrder.getProduct().equals(productToOrder)).findFirst();
            if(clientsOrderOptional.isPresent()) {
                ComponentClientsOrder order = clientsOrderOptional.get();
                order.setOrderedProductAmount(order.getOrderedProductAmount()+amount);
                order.setOrderedProductFullPrice(order.getOrderedProductFullPrice().add(
                                (productToOrder.getProductPrice().multiply(BigDecimal.valueOf(amount)))).setScale(2, RoundingMode.HALF_UP));
            } else {
                ComponentClientsOrder newComponentClientOrder = new ComponentClientsOrder(productToOrder,amount,
                        productToOrder.getProductPrice().multiply(BigDecimal.valueOf(amount)).setScale(2,RoundingMode.HALF_UP));
                componentClientsOrdersList.add(newComponentClientOrder);
            }
        } catch (CustomServiceException exception) {
            logger.error(exception.getMessage());
            redirectAttributes.addFlashAttribute("error",NONE_PRODUCTS_WERE_FOUND_DURING_SEARCH);
            return "redirect:/";
        }
        return "redirect:/catalog";
    }

    @GetMapping("/showBasket")
    public String showBasket(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<ComponentClientsOrder> componentClientsOrdersList = (List<ComponentClientsOrder>) session.getAttribute(COMPONENT_ORDERS_LIST);
        BigDecimal totalSum = BigDecimal.ZERO;
        if(componentClientsOrdersList != null && componentClientsOrdersList.size() != 0) {
            for(ComponentClientsOrder clientsOrder : componentClientsOrdersList) {
                totalSum = totalSum.add(clientsOrder.getOrderedProductFullPrice());
            }
        }
        model.addAttribute("totalSum",totalSum);
        return "basket";
    }

    @PostMapping("/alterOrder")
    public String alterOrder(Model model,
                             @RequestParam Long productId,
                             @RequestParam Long productAmount,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        List<ComponentClientsOrder> componentClientsOrdersList = (List<ComponentClientsOrder>) session.getAttribute(COMPONENT_ORDERS_LIST);
        if(componentClientsOrdersList != null) {
            Optional<ComponentClientsOrder> clientsOrderOptional = componentClientsOrdersList.stream().filter(componentClientsOrder -> componentClientsOrder.getProduct().getProductId().equals(productId)).findFirst();
            if(clientsOrderOptional.isPresent()) {
                ComponentClientsOrder clientsOrder = clientsOrderOptional.get();
                if(productAmount == 0) {
                    componentClientsOrdersList.remove(clientsOrder);
                } else {
                    clientsOrder.setOrderedProductAmount(productAmount);
                    clientsOrder.setOrderedProductFullPrice(BigDecimal.valueOf(productAmount).
                            multiply(clientsOrder.getProduct().getProductPrice()).
                            setScale(2,RoundingMode.HALF_UP));
                }
                return "redirect:/showBasket";
            } else {
                redirectAttributes.addFlashAttribute("error",ORDER_ALTER_ERROR);
                return "redirect:/";
            }
        } else {
            redirectAttributes.addFlashAttribute("error",EMPTY_ORDER_ERROR);
            return "redirect:/";
        }
    }

    @PostMapping("/orderProducts")
    public String orderProducts(Model model,
                                @RequestParam Optional<String> deliveryAddress,
                                @RequestParam Optional<Boolean> needDelivery,
                                @RequestParam BigDecimal totalPrice,
                                RedirectAttributes redirectAttributes,
                                Authentication authentication,
                                HttpServletRequest request) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        Optional<User> user = userService.findActiveUserByLogin(securityUser.getUsername());
        if(user.isPresent()) {
            HttpSession session = request.getSession();
            List<ComponentClientsOrder> componentClientsOrdersList = (List<ComponentClientsOrder>) session.getAttribute(COMPONENT_ORDERS_LIST);
            if(componentClientsOrdersList != null && componentClientsOrdersList.size()!=0) {
                deliveryAddress = needDelivery.isEmpty()?Optional.empty():deliveryAddress;
                BigDecimal deliveryPrice = deliveryAddress.isEmpty()?BigDecimal.ZERO: totalPrice.compareTo(BigDecimal.valueOf(30))>=0?BigDecimal.valueOf(5):BigDecimal.valueOf(10);
                if(clientsOrderValidator.validateClientOrder(componentClientsOrdersList)) {
                    clientOrderService.addClientOrder(componentClientsOrdersList,user.get(),deliveryAddress.orElse(NO_DELIVERY_ADDRESS),deliveryPrice);
                } else {
                    redirectAttributes.addFlashAttribute("error",ORDER_INVALID_PRODUCT_ERROR);
                    session.removeAttribute(COMPONENT_ORDERS_LIST);
                    return "redirect:/";
                }
                session.removeAttribute(COMPONENT_ORDERS_LIST);
                return "redirect:/catalog";
            } else {
                redirectAttributes.addFlashAttribute("error",EMPTY_ORDER_ERROR);
                return "redirect:/";
            }
        } else {
            redirectAttributes.addFlashAttribute("error",UNKNOWN_USER_ERROR);
            return "redirect:/";
        }
    }
}
