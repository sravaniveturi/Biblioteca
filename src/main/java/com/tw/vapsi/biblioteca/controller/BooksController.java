package com.tw.vapsi.biblioteca.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class BooksController {

    @GetMapping("books")
    public List<String> books() {
        return Arrays.asList("Book 1", "Book 2");
    }
}
