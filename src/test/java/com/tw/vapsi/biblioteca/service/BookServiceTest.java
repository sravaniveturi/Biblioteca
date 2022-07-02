package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.repository.BookRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {
    @MockBean
    BookRepository bookRepository;

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
    void shouldCallBookRepositoryUpdateCopies() {

        bookService.decrementBookCopyByOne(books);
        long bookId =books.get(0).getId();

        verify(bookRepository,times(1)).updateNoOfCopies(bookId);

    }
}