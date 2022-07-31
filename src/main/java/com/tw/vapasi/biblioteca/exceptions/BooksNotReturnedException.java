package com.tw.vapasi.biblioteca.exceptions;

public class BooksNotReturnedException extends Exception {

   public BooksNotReturnedException(String message){
       super("You cannot return the book : "+ message);
    }
}
