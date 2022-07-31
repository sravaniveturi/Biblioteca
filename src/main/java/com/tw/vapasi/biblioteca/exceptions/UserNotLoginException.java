package com.tw.vapasi.biblioteca.exceptions;

public class UserNotLoginException  extends Exception{

    public UserNotLoginException() {
        super("User is not logged in.");
    }
}
