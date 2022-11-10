package com.makkras.shop.controller;

import com.google.gson.Gson;
import com.makkras.shop.entity.SupplierCompany;
import com.makkras.shop.service.SupplierService;
import com.makkras.shop.service.impl.CustomSupplierService;
import com.makkras.shop.util.SupplierFilterUtil;
import com.makkras.shop.validator.SupplierDataValidator;
import com.makkras.shop.validator.impl.CustomSupplierDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class SupplierController {

    private static final String NONE_SUPPLIERS_FOUND_SEARCH_ERROR = "Поставщики, соответствующие параметрам поиска, не были найдены!";
    private static final String ONLY_ACTIVE_SUPPLIERS = "onlyActive";
    private static final String ONLY_INACTIVE_SUPPLIERS = "onlyInactive";
    private final Gson gson;
    private final SupplierService supplierService;
    private final SupplierDataValidator supplierDataValidator;
    private final SupplierFilterUtil supplierFilterUtil;

    @Autowired
    public SupplierController(CustomSupplierService supplierService,
                              CustomSupplierDataValidator supplierDataValidator,
                              SupplierFilterUtil supplierFilterUtil) {
        this.supplierService = supplierService;
        this.supplierDataValidator = supplierDataValidator;
        this.supplierFilterUtil = supplierFilterUtil;
        gson = new Gson();
    }

    @GetMapping("/suppliers")
    public String showSuppliers(Model model) {
        model.addAttribute("suppliers",gson.toJson(supplierService.getAllSupplierCompanies()));
        return "suppliers";
    }

    @PostMapping("/addSupplier")
    public String addSupplier(Model model,
                              @RequestParam String name,
                              @RequestParam String email,
                              @RequestParam String telNumber,
                              @RequestParam String location,
                              @RequestParam Optional<Boolean> isActive,
                              RedirectAttributes redirectAttributes) {
        SupplierCompany supplierCompanyToAdd = new SupplierCompany(name,email,telNumber,location,isActive.orElse(false));
        List<String> invalidParams = new ArrayList<>();
        if(supplierDataValidator.validateSupplierData(supplierCompanyToAdd,invalidParams)) {
            supplierService.addSupplier(supplierCompanyToAdd);
        } else {
            redirectAttributes.addFlashAttribute("error","Были введены неверные данные о поставщике: \n"+invalidParams.toString());
        }
        return "redirect:/suppliers";
    }

    @PostMapping("/updateSupplierData")
    public String updateSupplierData(Model model,
                                     @RequestParam Long supplier_id,
                                     @RequestParam String name,
                                     @RequestParam String email,
                                     @RequestParam String telNumber,
                                     @RequestParam String location,
                                     @RequestParam Optional<Boolean> isActive,
                                     RedirectAttributes redirectAttributes) {

        SupplierCompany supplierCompanyToUpdate = new SupplierCompany(supplier_id,name,email,telNumber,location,isActive.orElse(false));
        List<String> invalidParams = new ArrayList<>();
        if(supplierDataValidator.validateSupplierData(supplierCompanyToUpdate,invalidParams)) {
            if(!supplierService.updateSupplierData(supplierCompanyToUpdate)) {
                redirectAttributes.addFlashAttribute("error","Произошла ошибка! Данные не были обновлены!");
            }
        } else {
            redirectAttributes.addFlashAttribute("error","Были введены неверные данные о поставщике: \n"+invalidParams.toString());
        }
        return "redirect:/suppliers";
    }

    @PostMapping("/sortSuppliers")
    public String sortSuppliers(Model model, @RequestParam(required = false) String sortFormSelect) {
        String sortedSuppliersInGson;
        if(sortFormSelect == null || sortFormSelect.equals("noneSelected")) {
            sortedSuppliersInGson = gson.toJson(supplierService.getAllSupplierCompanies());
            model.addAttribute("error",ClientCatalogController.SORT_TYPE_NOT_SELECTED_ERROR);

        } else if(sortFormSelect.equals("byName")) {
            sortedSuppliersInGson = gson.toJson(supplierService.getAllSupplierCompaniesAndOrderByName());

        } else if(sortFormSelect.equals("byLocation")) {
            sortedSuppliersInGson = gson.toJson(supplierService.getAllSupplierCompaniesAndOrderByLocation());
        }else if(sortFormSelect.equals("byActivityDesc")) {
            sortedSuppliersInGson = gson.toJson(supplierService.getAllSupplierCompaniesAndOrderByIsActiveDesc());
        }else {
            sortedSuppliersInGson = gson.toJson(supplierService.getAllSupplierCompaniesAndOrderByIsActiveAsc());
        }
        model.addAttribute("suppliers",sortedSuppliersInGson);
        return "suppliers";
    }

    @PostMapping("/searchSuppliers")
    public String searchAndFilterSuppliers(Model model,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) String email,
                                           @RequestParam(required = false) String telNumber,
                                           @RequestParam(required = false) String location,
                                           @RequestParam String activityStatus) {
        List<SupplierCompany> allSuppliers = supplierService.getAllSupplierCompanies();
        List<SupplierCompany> filteredSuppliers = supplierFilterUtil.filterSuppliers(allSuppliers, name, email, telNumber, location);
        if(activityStatus.equals(ONLY_ACTIVE_SUPPLIERS)) {
            filteredSuppliers = filteredSuppliers.stream().filter(SupplierCompany::isActive).collect(Collectors.toList());
        } else if(activityStatus.equals(ONLY_INACTIVE_SUPPLIERS)) {
            filteredSuppliers = filteredSuppliers.stream().filter(supplierCompany -> !supplierCompany.isActive()).collect(Collectors.toList());
        }
        if(filteredSuppliers.size() == 0) {
            model.addAttribute("error",NONE_SUPPLIERS_FOUND_SEARCH_ERROR);
        }
        model.addAttribute("suppliers",gson.toJson(filteredSuppliers));
        return "suppliers";
    }

}
