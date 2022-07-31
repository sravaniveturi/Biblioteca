package com.tw.vapasi.biblioteca.controller;

import com.tw.vapasi.biblioteca.exceptions.BookAlreadyCheckoutException;
import com.tw.vapasi.biblioteca.exceptions.BookNotAvailableException;
import com.tw.vapasi.biblioteca.exceptions.BooksNotReturnedException;
import com.tw.vapasi.biblioteca.exceptions.UserNotLoginException;
import com.tw.vapasi.biblioteca.model.Book;
import com.tw.vapasi.biblioteca.model.User;
import com.tw.vapasi.biblioteca.service.BookService;
import com.tw.vapasi.biblioteca.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
public class BooksController {

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @GetMapping("/books")
    public String books(Model model, @RequestParam(required = false) String name) {
        List<Book> books = getBooks(name);
        model.addAttribute("user", new User());
        if (books.isEmpty()) {
            model.addAttribute("errorMessage", "Sorry No Books available in the library.");
            return "books";
        }
        model.addAttribute("books", books);
        return "books";
    }

    private List<Book> getBooks(String name) {
        if (name != null) {
            return bookService.findByBookNameOrAuthorName(name);
        }
        return bookService.books();
    }

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("User") User user, @AuthenticationPrincipal UserDetails currentUser,
                                 RedirectAttributes redirectAttrs)
            throws BookAlreadyCheckoutException, UserNotLoginException, BookNotAvailableException {

        List<Book> checkoutBooks = user.getCheckoutBooks();

        bookService.checkOut(checkoutBooks, currentUser.getUsername());
        redirectAttrs.addFlashAttribute("message", "Checkout books was Successful.");
        return "redirect:/viewCheckout";
    }

    @GetMapping("/viewCheckout")
    public String getCheckOutBooks(@AuthenticationPrincipal UserDetails user, Model model) throws UserNotLoginException {

        model.addAttribute("user", new User());
        List<Book> books = userService.getCheckOutBooks(user.getUsername());

        if (books.isEmpty()) {
            model.addAttribute("errorMessage", "No books checked out by the customer.");
        }
        model.addAttribute("books", books);
        return "viewcheckoutbooks";
    }

    @PostMapping("/return")
    public String returnBooks(@ModelAttribute("user") User user, @AuthenticationPrincipal UserDetails currentUser,
                              RedirectAttributes redirectAttrs)
            throws BooksNotReturnedException, UserNotLoginException {

        List<Book> returnBooks = user.getCheckoutBooks();

        int numBooksReturned = bookService.returnBooks(returnBooks, currentUser.getUsername());
        redirectAttrs.addFlashAttribute("message",  getSuccessMessage(numBooksReturned));
        return "redirect:/viewCheckout";

    }

    private String getSuccessMessage(int numBooksReturned) {
        if(numBooksReturned == 1) {
            return "1 book was returned successfully.";
        }
            return numBooksReturned+ " books were returned  successfully.";
    }


}