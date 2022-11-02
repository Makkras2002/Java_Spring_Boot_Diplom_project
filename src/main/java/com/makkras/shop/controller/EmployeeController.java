package com.makkras.shop.controller;

import com.google.gson.Gson;
import com.makkras.shop.entity.Product;
import com.makkras.shop.entity.ProductCategory;
import com.makkras.shop.service.ProductService;
import com.makkras.shop.service.impl.CustomProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Optional;

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

    @PostMapping("/updateProductData")
    public String updateProductData(Model model,
                                    @RequestParam Long product_id,
                                    @RequestParam String name,
                                    @RequestParam String category,
                                    @RequestParam BigDecimal price,
                                    @RequestParam String picture,
                                    @RequestParam String comment,
                                    @RequestParam Long amount,
                                    @RequestParam Optional<Boolean> isAvailable) {
        System.out.println(new Product(product_id,name,price,amount,picture,comment,new ProductCategory(category), isAvailable.orElse(false)));
        return "redirect:/employeeMain";
    }
}
