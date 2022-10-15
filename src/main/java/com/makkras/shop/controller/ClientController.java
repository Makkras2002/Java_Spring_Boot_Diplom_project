package com.makkras.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {
    @GetMapping("/catalog")
    public String showCatalog(Model model) {
        return "catalog";
    }
}
