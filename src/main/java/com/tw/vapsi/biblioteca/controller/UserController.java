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
    public RedirectView checkout(@ModelAttribute("user")User user , Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        user.setEmail(userDetails.getUsername());
        List<Book> books=userService.checkOut(user);
        return new RedirectView("/");
    }

    @GetMapping("/viewCheckout")
    public ModelAndView getCheckOutBooks(@AuthenticationPrincipal UserDetails user){

         ModelAndView mav = new ModelAndView("bookdetails");
            List<Book> books = userService.getCheckOutBooks(user.getUsername());
            if (books.isEmpty()) {
                mav.addObject("errorMessage", "No books checked out by user.");
            }
            mav.addObject("books", books);
        return mav;
    }

}
