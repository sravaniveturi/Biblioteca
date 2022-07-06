package com.tw.vapsi.biblioteca.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Book book ;
    @BeforeEach
    void setUp(){
        book=new Book(1, "Harry Potter", "J.K Rowling", 2000, 5)  ;
    }
    @Test
    void shouldIncrementCopiesOfBooks() {
        book.incrementCopies();
        assertEquals(6,book.getNumOfCopies());
    }

    @Test
    void shouldDecrementCopies(){
        book.decrementCopies();
        assertEquals(4, book.getNumOfCopies());
    }

    @Test
    void cannotDecrementWhenBookHasNoCopy(){
        book.setNumOfCopies(0);
        book.decrementCopies();
        assertEquals(0, book.getNumOfCopies());
    }

}