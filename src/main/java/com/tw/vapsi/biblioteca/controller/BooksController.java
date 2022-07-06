package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.service.BookService;
import com.tw.vapsi.biblioteca.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller

public class BooksController {

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @GetMapping("/books")
    public String books(Model model, @RequestParam Optional<String> name) {
        List<Book> books = getBooks(name);
        model.addAttribute("user", new User());
        if (books.isEmpty()) {
            model.addAttribute("errorMessage", "Sorry No Books available.");
            return "books";
        }
        model.addAttribute("books", books);
        return "books";
    }

    private List<Book> getBooks(Optional<String> name) {
        if (name.isPresent()) {
            return bookService.findByBookNameOrAuthorName(name.get());
        }
        return bookService.books();
    }

    @PostMapping("/checkout")
    public ModelAndView checkout(@ModelAttribute("User") User user, @AuthenticationPrincipal UserDetails currentUser,RedirectAttributes redirectAttrs) {
        ModelAndView mav;
        List<Book> checkoutBooks = user.getCheckoutBooks();
        try {
            List<Book> books = bookService.checkOut(checkoutBooks, currentUser.getUsername());
            ResponseEntity.status(HttpStatus.OK);
            redirectAttrs.addFlashAttribute("message","Checkout books was Successful");
            mav = new ModelAndView("redirect:/viewCheckout");
            //mav.addObject("successMessage", "Checkout books was Successful");

        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("message",e.getMessage());
            mav = new ModelAndView("redirect:/books");
           // mav.addObject("errorMessage",e.getMessage());
        }
        return mav;
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
    public String returnBooks(@ModelAttribute("user") User userWithCheckedOutBooks, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttrs) {
        userWithCheckedOutBooks.setEmail(userDetails.getUsername());
        String successMessage = bookService.returnBooks(userWithCheckedOutBooks);


        redirectAttrs.addFlashAttribute("success", successMessage);

        return "redirect:/viewCheckout";
    }


}