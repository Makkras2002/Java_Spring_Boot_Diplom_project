package com.makkras.shop.controller;

import com.google.gson.Gson;
import com.makkras.shop.entity.Product;
import com.makkras.shop.entity.ProductCategory;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.service.ProductService;
import com.makkras.shop.service.impl.CustomProductService;
import com.makkras.shop.util.FileUploadUtil;
import com.makkras.shop.util.ProductFilterUtil;
import com.makkras.shop.validator.ProductDataValidator;
import com.makkras.shop.validator.impl.CustomProductDataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class EmployeeMainController {
    private static final String PRODUCT_PICTURES_LOCATION_DIR = "prod_pics/";
    private static final String ONLY_AVAILABLE_PRODUCTS = "onlyAvailable";
    private static final String ONLY_UNAVAILABLE_PRODUCTS = "onlyUnavailable";
    private static final Logger logger = LogManager.getLogger();
    private final ProductService productService;
    private final ProductDataValidator productDataValidator;
    private final FileUploadUtil fileUploadUtil;
    private final ProductFilterUtil productFilterUtil;
    private final Gson gson;

    @Autowired
    public EmployeeMainController(CustomProductService productService, CustomProductDataValidator productDataValidator,
                                  FileUploadUtil fileUploadUtil, ProductFilterUtil productFilterUtil) {
        this.productService = productService;
        this.productDataValidator = productDataValidator;
        this.fileUploadUtil = fileUploadUtil;
        this.productFilterUtil = productFilterUtil;
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
        String fileName = "";
        if(picture != null) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(picture.getOriginalFilename()));
            try {
                fileUploadUtil.saveFile(PRODUCT_PICTURES_LOCATION_DIR, fileName, picture);
            } catch (IOException exception) {
                logger.error(exception.getMessage());
            }
        }

        Product productToAdd = new Product(name,price,amount,fileName,comment,new ProductCategory(category), isAvailable.orElse(false));
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
        String fileName = "";
        if(picture != null) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(picture.getOriginalFilename()));
        }
        Product productToUpdate = new Product(product_id,name,price,amount,fileName,comment,new ProductCategory(category), isAvailable.orElse(false));
        if(productDataValidator.validateProductData(productToUpdate)) {
            try {
                if(!productService.updateProductData(productToUpdate,PRODUCT_PICTURES_LOCATION_DIR,picture)) {
                    redirectAttributes.addFlashAttribute("error","Произошла ошибка! Данные не были обновлены!");
                }
            } catch (CustomServiceException exception) {
                logger.error(exception.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("error","Были введены неверные данные о продукте!");
        }
        return "redirect:/employeeMain";
    }

    @PostMapping("/sortAllProducts")
    public String sortCatalog(Model model, @RequestParam(required = false) String sortFormSelect) {
        String sortedProductsInGson;
        if(sortFormSelect == null || sortFormSelect.equals("noneSelected")) {
            sortedProductsInGson = gson.toJson(productService.getAllProducts());
            model.addAttribute("error", ClientCatalogController.SORT_TYPE_NOT_SELECTED_ERROR);

        } else if(sortFormSelect.equals("byPriceAsc")) {
            sortedProductsInGson = gson.toJson(productService.getAllProductsAndOrderByPriceAsc());
        } else if(sortFormSelect.equals("byPriceDesc")) {
            sortedProductsInGson = gson.toJson(productService.getAllProductsAndOrderByPriceDesc());
        } else if(sortFormSelect.equals("byName")) {
            sortedProductsInGson = gson.toJson(productService.getAllProductsAndOrderByName());
        } else if(sortFormSelect.equals("byAvailabilityDesc")) {
            sortedProductsInGson = gson.toJson(productService.getAllProductsAndOrderByIsAvailableDesc());
        } else if(sortFormSelect.equals("byAvailabilityAsc")) {
            sortedProductsInGson = gson.toJson(productService.getAllProductsAndOrderByIsAvailableAsc());
        } else {
            sortedProductsInGson = gson.toJson(productService.getAllProductsAndOrderByCategory());
        }
        model.addAttribute("allProducts",sortedProductsInGson);
        model.addAttribute("productCategories",gson.toJson(productService.getAllProductCategories()));
        model.addAttribute("categories",productService.getAllProductCategories());
        return "employeeMain";
    }

    @PostMapping("/searchAllProducts")
    public String searchAndFilterInEmployeeMain(Model model,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) String category,
                                           @RequestParam(required = false) BigDecimal minPrice,
                                           @RequestParam(required = false) BigDecimal maxPrice,
                                           @RequestParam String availabilityStatus) {
        List<Product> allProducts = productService.getAllProducts();
        List<Product> filteredProducts = productFilterUtil.filter(allProducts,name,category,minPrice,maxPrice);
        if(availabilityStatus.equals(ONLY_AVAILABLE_PRODUCTS)) {
            filteredProducts = filteredProducts.stream().filter(Product::isAvailable).collect(Collectors.toList());
        } else if(availabilityStatus.equals(ONLY_UNAVAILABLE_PRODUCTS)) {
            filteredProducts = filteredProducts.stream().filter(product -> !product.isAvailable()).collect(Collectors.toList());
        }
        if(filteredProducts.size()==0) {
            model.addAttribute("error", ClientCatalogController.NONE_PRODUCTS_WERE_FOUND_DURING_SEARCH);
        }
        String filteredProductsDataInGson = gson.toJson(filteredProducts);
        model.addAttribute("allProducts",filteredProductsDataInGson);
        model.addAttribute("productCategories",gson.toJson(productService.getAllProductCategories()));
        model.addAttribute("categories",productService.getAllProductCategories());
        return "employeeMain";
    }
}
