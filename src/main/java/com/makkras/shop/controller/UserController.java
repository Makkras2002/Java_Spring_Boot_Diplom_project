package com.makkras.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @GetMapping("/login")
    public String performLogin(Model model) {
        return "/loginPage";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        return "registrationPage";
    }

    @PostMapping("/register")
    public String performRegistration(Model model) {
        return "startPage";
    }

}
