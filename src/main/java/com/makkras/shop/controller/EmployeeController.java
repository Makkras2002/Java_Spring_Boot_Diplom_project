package com.makkras.shop.controller;

import com.google.gson.Gson;
import com.makkras.shop.entity.Product;
import com.makkras.shop.entity.ProductCategory;
import com.makkras.shop.service.ProductService;
import com.makkras.shop.service.impl.CustomProductService;
import com.makkras.shop.validator.ProductDataValidator;
import com.makkras.shop.validator.impl.CustomProductDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.annotation.MultipartConfig;
import java.math.BigDecimal;
import java.util.Optional;

@Controller
public class EmployeeController {
    private static final String DEFAULT_PRODUCT_PICTURE = "default_picture";
    private static final String PRODUCT_PICTURES_LOCATION = "pictures/product_pictures/";
    private final ProductService productService;
    private final ProductDataValidator productDataValidator;
    private final Gson gson;

    @Autowired
    public EmployeeController(CustomProductService productService, CustomProductDataValidator productDataValidator) {
        this.productService = productService;
        this.productDataValidator = productDataValidator;
        gson = new Gson();
    }

    @GetMapping("/employeeMain")
    public String showMainEmployeePage(Model model) {
        model.addAttribute("allProducts",gson.toJson(productService.getAllProducts()));
        model.addAttribute("productCategories",gson.toJson(productService.getAllProductCategories()));
        model.addAttribute("categories",productService.getAllProductCategories());
        return "employeeMain";
    }

    @PostMapping("/addProduct")
    public String addProduct(Model model,
                             @RequestParam String name,
                             @RequestParam String category,
                             @RequestParam BigDecimal price,
                             @RequestParam MultipartFile picture,
                             @RequestParam String comment,
                             @RequestParam Long amount,
                             @RequestParam Optional<Boolean> isAvailable,
                             RedirectAttributes redirectAttributes) {
//        if(picture.getOriginalFilename()!=null && !picture.getOriginalFilename().equals(DEFAULT_PRODUCT_PICTURE)) {
//            productService.createNewPictureForProductInResources(picture);
//        }
        Product productToAdd = new Product(name,price,amount,PRODUCT_PICTURES_LOCATION+picture.getOriginalFilename(),comment,new ProductCategory(category), isAvailable.orElse(false));
        if(productDataValidator.validateProductData(productToAdd)) {
            productService.addProduct(productToAdd);
        } else {
            redirectAttributes.addFlashAttribute("error","Были введены неверные данные о продукте!");
        }
        return "redirect:/employeeMain";
    }

    @PostMapping("/updateProductData")
    public String updateProductData(Model model,
                                    @RequestParam Long product_id,
                                    @RequestParam String name,
                                    @RequestParam String category,
                                    @RequestParam BigDecimal price,
                                    @RequestParam MultipartFile picture,
                                    @RequestParam String comment,
                                    @RequestParam Long amount,
                                    @RequestParam Optional<Boolean> isAvailable,
                                    RedirectAttributes redirectAttributes) {
        Product productToUpdate = new Product(product_id,name,price,amount,picture.getOriginalFilename(),comment,new ProductCategory(category), isAvailable.orElse(false));
        if(productDataValidator.validateProductData(productToUpdate)) {
            if(!productService.updateProductData(productToUpdate)) {
                redirectAttributes.addFlashAttribute("error","Произошла ошибка! Данные не были обновлены");
            }
        } else {
            redirectAttributes.addFlashAttribute("error","Были введены неверные данные о продукте!");
        }
        return "redirect:/employeeMain";
    }
}
