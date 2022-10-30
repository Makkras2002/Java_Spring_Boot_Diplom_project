package com.makkras.shop.controller;

import com.google.gson.Gson;
import com.makkras.shop.entity.Product;
import com.makkras.shop.service.ProductService;
import com.makkras.shop.service.impl.CustomProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ClientController {
    private static final String SORT_TYPE_NOT_SELECTED_ERROR = "Способ сортировки не был выбран!";
    private static final String NONE_PRODUCTS_WERE_FOUND_DURING_SEARCH = "Продукты, соответствующие параметрам поиска, не были найдены!";
    private final ProductService productService;
    private final Gson gson;

    @Autowired
    public ClientController(CustomProductService productService) {
        this.productService = productService;
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
        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> name==null || (product.getProductName().toLowerCase().contains(name.toLowerCase()) && name.length()>2) || name.length()<=2)
                .filter(product -> category==null || product.getCategory().getCategory().equals(category) || category.equals("-"))
                .filter(product -> minPrice== null || product.getProductPrice().doubleValue()>=minPrice.doubleValue())
                .filter(product -> maxPrice==null || product.getProductPrice().doubleValue()<= maxPrice.doubleValue())
                .collect(Collectors.toList());
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
}
