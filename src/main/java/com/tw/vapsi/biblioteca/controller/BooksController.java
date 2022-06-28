package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController("api/v1/books")
public class BooksController {

    BookService bookService;

    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<String> books() {
        return Arrays.asList("Book 1", "Book 2");
    }
}
