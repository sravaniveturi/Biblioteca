package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.exceptions.BookAlreadyCheckoutException;
import com.tw.vapsi.biblioteca.exceptions.BooksNotReturnedException;
import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.repository.BookRepository;
import com.tw.vapsi.biblioteca.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {
    @MockBean
    BookRepository bookRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    BookService bookService;
    List<Book> books;

    @BeforeEach
    void setUp() {
        books = Arrays.asList(new Book(1, "Nancy Drew", "Carolyn keene", 1988, 1));
    }

    @Test
    void shouldReturnAllBooksInLibrary() {
        when(bookRepository.findAll(any(Sort.class))).thenReturn(books);
        List<Book> booksReturned = bookService.books();
        assertEquals(booksReturned, books);
    }

    @Test
    void shouldReturnEmptyListWhenNoBooksAvailable() {
        books = new ArrayList<>();
        when(bookRepository.findAll(any(Sort.class))).thenReturn(books);
        List<Book> booksReturned = bookService.books();
        assertEquals(booksReturned, books);
    }

    @Test
    void shouldReturnBookWhenFindByBookName() {
        when(bookRepository.findByBookNameContainingIgnoreCase(any())).thenReturn(books);

        List<Book> booksReturned = bookService.findByBookNameOrAuthorName("Nancy Drew");

        assertEquals(booksReturned, books);
    }

    @Test
    void shouldReturnEmptyWhenFindByBookNameNotSuccessFul() {
        when(bookRepository.findByBookNameContainingIgnoreCase(any())).thenReturn(Lists.newArrayList());

        List<Book> booksReturned = bookService.findByBookNameOrAuthorName("Harry");

        assertTrue(booksReturned.isEmpty());
    }

    @Test
    void shouldReturnBookWhenFindByAuthorName() {
        when(bookRepository.findByAuthorNameContainingIgnoreCase(any())).thenReturn(books);

        List<Book> booksReturned = bookService.findByBookNameOrAuthorName("Carolyn");

        assertEquals(booksReturned, books);
    }

    @Test
    void shouldReturnEmptyWhenFindByAuthorNameNotSuccessFul() {
        when(bookRepository.findByAuthorNameContainingIgnoreCase(any())).thenReturn(Lists.newArrayList());

        List<Book> booksReturned = bookService.findByBookNameOrAuthorName("Carolyn");

        assertTrue(booksReturned.isEmpty());
    }

    @Test
    void shouldCheckoutBooks() throws BookAlreadyCheckoutException {
        User user = mock(User.class);
        List<Book> checkoutBooks = Arrays.asList(new Book());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        List<Book> userCheckoutBooks = bookService.checkOut(checkoutBooks, user.getEmail());

        assertEquals(userCheckoutBooks, user.getCheckoutBooks());
    }

    @Test
    void cannotCheckoutAlreadyCheckoutBooks() throws BookAlreadyCheckoutException {
        User user = mock(User.class);
        List<Book> checkoutBooks = Arrays.asList(new Book());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(user.getCheckoutBooks()).thenReturn(checkoutBooks);

        assertThrows(BookAlreadyCheckoutException.class, ()->bookService.checkOut(checkoutBooks, user.getEmail()));
    }


    @Test
    void shouldReturnBooksIfCheckedOut() throws BooksNotReturnedException {
        User user = new User(
                "Micky",
                "Mouse",
                "micky-mouse@example.com",
                "encoded-password");

        User userFromDB = new User(
                "Micky",
                "Mouse",
                "micky-mouse@example.com",
                "encoded-password");

        List<Book> books = Arrays.asList(new Book(1, "Harry Potter", "J.K Rowling", 2000, 1));
        user.setCheckoutBooks(books);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(userFromDB));
        when(userRepository.save(any())).thenReturn(userFromDB);
        when(bookRepository.saveAll(any())).thenReturn(books);
        String successMessage = bookService.returnBooks(user);
        assertEquals("1 book returned successfully .", successMessage);
        verify(userRepository, times(1)).findByEmail(any());
        verify(userRepository, times(1)).save(any());
        verify(bookRepository, times(1)).saveAll(any());

    }
    @Test
    void shouldThowExceptionWhenBooksNotReturnedSuccessfully()  {
        User user = new User(
                "Micky",
                "Mouse",
                "micky-mouse@example.com",
                "encoded-password");
        User userFromDB = new User(
                "Micky",
                "Mouse",
                "micky-mouse@example.com",
                "encoded-password");
        List<Book> books = Lists.newArrayList(new Book(1, "Harry Potter", "J.K Rowling", 2000, 1));
        user.setCheckoutBooks(books);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(userFromDB));
        when(userRepository.save(any())).thenReturn(null);

        assertThrows(BooksNotReturnedException.class,()-> bookService.returnBooks(userFromDB));
    }
}