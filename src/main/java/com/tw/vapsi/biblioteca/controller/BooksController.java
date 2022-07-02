package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.service.BookService;
import com.tw.vapsi.biblioteca.service.dto.UserDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        model.addAttribute("user",new User());

        return "books";
    }

    @GetMapping("/findbook")
    public String getBookByBookName(Model model, @RequestParam String name){
        List<Book> books = bookService.findByBookNameOrAuthorName(name);
        model.addAttribute("books", books);
        return "findbook";
    }

}