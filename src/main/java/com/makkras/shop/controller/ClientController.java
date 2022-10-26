package com.makkras.shop.controller;

import com.google.gson.Gson;
import com.makkras.shop.service.ProductService;
import com.makkras.shop.service.impl.CustomProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {
    private ProductService productService;
    private Gson gson;

    @Autowired
    public ClientController(CustomProductService productService) {
        this.productService = productService;
        this.gson = new Gson();
    }

    @GetMapping("/catalog")
    public String showCatalog(Model model) {
        String productsInStockDataInGson = gson.toJson(productService.getAllAvailableAndInStockProducts());
        model.addAttribute("productsInStock", productsInStockDataInGson);
        return "catalog";
    }
}
