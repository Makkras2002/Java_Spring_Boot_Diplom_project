package com.makkras.shop.controller;

import com.google.gson.Gson;
import com.makkras.shop.service.ProductService;
import com.makkras.shop.service.impl.CustomProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeeController {
    private final ProductService productService;
    private final Gson gson;

    @Autowired
    public EmployeeController(CustomProductService productService) {
        this.productService = productService;
        gson = new Gson();
    }

    @GetMapping("/employeeMain")
    public String showMainEmployeePage(Model model) {
        model.addAttribute("allProducts",gson.toJson(productService.getAllProducts()));
        model.addAttribute("productCategories",gson.toJson(productService.getAllProductCategories()));
        return "employeeMain";
    }
}
