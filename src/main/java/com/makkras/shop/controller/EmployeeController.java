package com.makkras.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeeController {

    @GetMapping("/employeeMain")
    public String showMainEmployeePage(Model model) {
        return "employee_pages/employeeMain";
    }
}
