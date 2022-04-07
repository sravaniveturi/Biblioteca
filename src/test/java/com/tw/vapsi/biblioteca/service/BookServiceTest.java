package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    private BookService bookService;


    @BeforeEach
    void setUp() {
        bookService = new BookService(bookRepository);
    }

    @Test
    void shouldFetchAllTheBooks() {
        List<Book> expectedBooks = Arrays.asList(Book.builder().name("Abc").build(), Book.builder().name("Xyz").build());
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.fetchAllBooks();
        assertEquals(2, actualBooks.size());
        assertEquals(expectedBooks, actualBooks);

    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoBooks() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        assertEquals(0, bookService.fetchAllBooks().size());
    }
}