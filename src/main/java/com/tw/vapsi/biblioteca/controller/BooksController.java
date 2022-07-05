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
import java.util.Optional;

@Controller

public class BooksController {

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @GetMapping("/books")
    public String books(Model model,@RequestParam Optional<String> name) {
        List<Book> books = getBooks(name);
        model.addAttribute("user", new User());
        if (books.isEmpty()) {
            model.addAttribute("message", "Sorry No Books available.");
            return "books";
        }
        model.addAttribute("books", books);
        return "books";
    }

    private List<Book> getBooks(Optional<String> name){
        if (name.isPresent()){
            return bookService.findByBookNameOrAuthorName(name.get());
        }
        return bookService.books();
    }

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("User") User user, @AuthenticationPrincipal UserDetails currentUser) {
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
        mav.addObject("user", new User());
        List<Book> books = userService.getCheckOutBooks(user.getUsername());
        if (books.isEmpty()) {
            mav.addObject("errorMessage", "No books checked out by user.");
        }
        mav.addObject("books", books);
        return mav;
    }
    @PostMapping("/return")
    public String returnBooks(@ModelAttribute("user") User userWithCheckedOutBooks, @AuthenticationPrincipal UserDetails userDetails) {
        userWithCheckedOutBooks.setEmail(userDetails.getUsername());
        bookService.returnBooks(userWithCheckedOutBooks);
        return "redirect:/users/viewCheckout";
    }



}