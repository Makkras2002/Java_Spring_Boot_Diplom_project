package com.makkras.shop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
