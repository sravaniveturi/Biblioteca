package com.tw.vapasi.biblioteca.controller;

import com.tw.vapasi.biblioteca.model.User;
import com.tw.vapasi.biblioteca.service.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {

    private final UserService userService;
    private User user;
    private Model model;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User createUser(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam String password) {
        return userService.save(firstName, lastName, email, password);
    }


    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String createUser(@ModelAttribute("user") User user, Model model){
        User createdUser = userService.save(user.getFirstName(), user.getLastName(),user.getEmail(), user.getPassword());
        if(createdUser != null) {
            model.addAttribute("message", "You have successfully signed up!");
        }
        return "signup";
    }
}



