package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.service.BookService;
import com.tw.vapsi.biblioteca.service.UserService;
import org.jetbrains.annotations.NotNull;
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

    @Autowired
    UserService userService;

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
        return "findbook";
    }

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("User") @NotNull User user, @AuthenticationPrincipal UserDetails currentUser) {
        List<Book> checkoutBooks = user.getCheckoutBooks();
        boolean status= bookService.updateCopies(checkoutBooks);
       if(status) {
           userService.checkOut(checkoutBooks, currentUser.getUsername());
           return "redirect:/viewCheckout";
       }
       return "/books";
    }

    @GetMapping("/viewCheckout")
    public ModelAndView getCheckOutBooks(@AuthenticationPrincipal UserDetails user) {

        ModelAndView mav = new ModelAndView("viewcheckoutbooks");
        List<Book> books = userService.getCheckOutBooks(user.getUsername());
        if (books.isEmpty()) {
            mav.addObject("errorMessage", "No books checked out by user.");
        }
        mav.addObject("books", books);
        return mav;
    }

}