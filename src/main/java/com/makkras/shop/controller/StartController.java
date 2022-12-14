package com.makkras.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartController {

    @GetMapping("/")
    public String showStartPage(Model model) {

        return "startPage";
    }
    @GetMapping("/aboutUs")
    public String showAboutUsPage(Model model) {
        return "aboutUs";
    }
}
