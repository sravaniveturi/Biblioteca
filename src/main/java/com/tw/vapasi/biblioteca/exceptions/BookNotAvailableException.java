package com.tw.vapasi.biblioteca.exceptions;

public class BookNotAvailableException extends Exception {

    public BookNotAvailableException(String message) {
        super("Sorry book : "+ message+" not available for checkout.");
    }
}
