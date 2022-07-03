package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam String password) {
        return userService.save(firstName, lastName, email, password);
    }

    @PostMapping("/checkout")
    public ModelAndView checkout(@ModelAttribute("user")User userWithCheckedOutBooks ,@AuthenticationPrincipal UserDetails userDetails) {
        userWithCheckedOutBooks.setEmail(userDetails.getUsername());
        List<Book> books=userService.checkOut(userWithCheckedOutBooks);
        ModelAndView mav = new ModelAndView("index");
        String welcomeText = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
        mav.addObject("welcomeText", welcomeText);
        return mav;



       // return new RedirectView("/");

    }

    @GetMapping("/viewCheckout")
    public ModelAndView getCheckOutBooks(@AuthenticationPrincipal UserDetails user){

         ModelAndView mav = new ModelAndView("viewcheckoutbooks");
            List<Book> books = userService.getCheckOutBooks(user.getUsername());
            if (books.isEmpty()) {
                mav.addObject("errorMessage", "No books checked out by user.");
            }
            mav.addObject("books", books);
        return mav;
    }

}
