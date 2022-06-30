package com.tw.vapsi.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getWelcome(Model model) {
        String welcomeText = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
        model.addAttribute("welcomeText", welcomeText);
        return "index";

    }

}



