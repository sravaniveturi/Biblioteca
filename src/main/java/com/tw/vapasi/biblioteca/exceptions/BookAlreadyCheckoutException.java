package com.tw.vapasi.biblioteca.exceptions;

public class BookAlreadyCheckoutException  extends Exception{

    public BookAlreadyCheckoutException(String message) {
        super("You have already checkout the book: "+ message);
    }
}
