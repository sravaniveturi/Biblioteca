package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping()
public class BooksController {

    @Autowired
    BookService bookService;

    @GetMapping("/books")
    public String books(Model model) {
        List<Book> books = bookService.books();
        model.addAttribute("books",books);
        return "books";
    }

    @GetMapping("/books/find")
    public String getBookByBookNameOrAuthorName(Model model, @RequestParam String name){
        Book book = bookService.findByBookNameOrAuthorName(name);
        model.addAttribute("book", book);
        return "bookdetails";
    }


}