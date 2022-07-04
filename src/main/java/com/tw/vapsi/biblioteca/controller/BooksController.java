package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller

public class BooksController {

    @Autowired
    BookService bookService;

    @GetMapping("/books")
    public String books(Model model) {
        List<Book> books = bookService.books();
        model.addAttribute("user", new User());
        if (books.isEmpty()) {
            model.addAttribute("message", "Sorry no books available");
            return "books";
        }
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/findbook")
    public String getBookByBookName(Model model, @RequestParam String name) {
        List<Book> books = bookService.findByBookNameOrAuthorName(name);
        model.addAttribute("books", books);
        return "viewcheckoutbooks";
    }

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("user") User userWithCheckedOutBooks, @AuthenticationPrincipal UserDetails userDetails) {
        userWithCheckedOutBooks.setEmail(userDetails.getUsername());
        List<Book> books = bookService.checkOut(userWithCheckedOutBooks);
        return "redirect:/users/viewCheckout";



    }

}