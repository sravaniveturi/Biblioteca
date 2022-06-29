package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookServiceTest {
    @MockBean
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @Test
    void findByBookName() {
        Book book = new Book(1, "Nancy Drew", "Carolyn keene", 1988, 1);
        when(bookRepository.findByBookName(any())).thenReturn(Optional.of(book));

        Book bookReturned = bookService.findByBookNameOrAuthorName("Nancy Drew");

        assertEquals(bookReturned, book);
    }

    @Test
    void shouldReturnAllBooksInLibrary(){
        List<Book> books = Arrays.asList(new Book(1, "Nancy Drew", "Carolyn keene", 1988, 1), new Book(2, "Harry Potter", "J.K Rowling", 1988, 1));
        when(bookRepository.findAll(any(Sort.class))).thenReturn(books);
        List<Book> booksReturned = bookService.books();
        assertEquals(booksReturned, books);
    }

}