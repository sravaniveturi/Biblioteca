package com.tw.vapsi.biblioteca.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String base() {
        return "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
    }
}
