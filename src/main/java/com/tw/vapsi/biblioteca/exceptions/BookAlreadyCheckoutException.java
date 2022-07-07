package com.tw.vapsi.biblioteca.exceptions;

public class BookAlreadyCheckoutException  extends Exception{

    public BookAlreadyCheckoutException(String message) {
        super(message);
    }
}
