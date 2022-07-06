package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.service.UserService;
import org.springframework.web.bind.annotation.*;


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


}



