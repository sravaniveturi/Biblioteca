package com.tw.vapasi.biblioteca.model;

import com.tw.vapasi.biblioteca.exceptions.BookAlreadyCheckoutException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldAddCheckoutBook() throws BookAlreadyCheckoutException {
        Book book = new Book();
        User user = new User();

        user.addCheckoutBook(book);

        assertTrue(user.getCheckoutBooks().contains(book));
    }

    @Test
    void shouldRemoveBooksReturnedFromCheckedOutBooks() {
        Book book1=new Book(1, "Harry Potter", "J.K Rowling", 2000, 1);
        Book book2=new Book(2, "The Power of Your Subconscious Mind", "Joseph Murphy", 2000, 1);
        Book book3=new Book(3, "Word Power Made Easy", " Norman Lewis", 2000, 1);
        Book book4=new Book(4, "You Can", "George Matthew Adams", 2000, 1);
        Book book5=new Book(5, "The Blue Umbrella", "Ruskin Bond ", 2000, 1);
        List<Book> checkedOutBookList= Lists.newArrayList(book1,book2,book3,book4,book5);
        List<Book> returnedBookList= Lists.newArrayList(book1,book2);
        List<Book> expectedBookList= Lists.newArrayList(book3,book4,book5);
        User userFromDB= new User();
        User userFromScreen= new User();
        userFromDB.setCheckoutBooks(checkedOutBookList);
        userFromScreen.setCheckoutBooks(returnedBookList);
       // userFromDB.returnBooks(userFromScreen);
        assertEquals(expectedBookList,userFromDB.getCheckoutBooks());
    }

    @Test
    void shouldReturnNoOfBooksReturned(){
        Book book1=new Book(1, "Harry Potter", "J.K Rowling", 2000, 1);
        Book book2=new Book(2, "The Power of Your Subconscious Mind", "Joseph Murphy", 2000, 1);

        List<Book> returnedBookList= Lists.newArrayList(book1,book2);

        User user= new User();
        user.setCheckoutBooks(returnedBookList);
       // assertEquals(2,user.getNoOfBooksReturned());
    }

    @Test
    void shouldIncrementCopiesOfBooksReturned(){

        Book book1=new Book(1, "Harry Potter", "J.K Rowling", 2000, 1);
        Book book2=new Book(2, "The Power of Your Subconscious Mind", "Joseph Murphy", 2000, 1);

        List<Book> returnedBookList= Lists.newArrayList(book1,book2);

        User user= new User();
        user.setCheckoutBooks(returnedBookList);
        //user.incrementCopiesOfReturnedBook();
        assertEquals(2,book1.getNumOfCopies());
        assertEquals(2,book2.getNumOfCopies());


    }
}