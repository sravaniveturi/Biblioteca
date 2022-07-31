package com.tw.vapasi.biblioteca.controller.advisor;

import com.tw.vapasi.biblioteca.exceptions.BookAlreadyCheckoutException;
import com.tw.vapasi.biblioteca.exceptions.BookNotAvailableException;
import com.tw.vapasi.biblioteca.exceptions.BooksNotReturnedException;
import com.tw.vapasi.biblioteca.exceptions.UserNotLoginException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class BooksControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler({BookAlreadyCheckoutException.class, BookNotAvailableException.class})
    public ModelAndView handleBookCheckoutException(Exception exception, RedirectAttributes redirectAttrs){
        ModelAndView mav = new ModelAndView("redirect:/books");

        redirectAttrs.addFlashAttribute("message",exception.getMessage());
        ResponseEntity.status(HttpStatus.BAD_REQUEST);
        return mav;
    }

    @ExceptionHandler(BooksNotReturnedException.class)
    public ModelAndView handleBooksNotReturnedExceptionException(Exception exception, RedirectAttributes redirectAttrs){
        ModelAndView mav = new ModelAndView("redirect:/viewCheckout");

        redirectAttrs.addFlashAttribute("message",exception.getMessage());
        ResponseEntity.status(HttpStatus.BAD_REQUEST);
        return mav;
    }

    @ExceptionHandler(UserNotLoginException.class)
    public String handleUserNotLoginException(){

        ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        return  "redirect:/ login";
    }
}
