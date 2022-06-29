package com.tw.vapsi.biblioteca.controller;


import com.tw.vapsi.biblioteca.controller.helper.ControllerTestHelper;
import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = BooksController.class)
class BooksControllerTest extends ControllerTestHelper {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BookService bookService;

    @Test
    @WithMockUser
    void shouldReturnListOfBooksIfAvailable() throws Exception {

        List<Book> books = Arrays.asList(new Book(1, "Harry Potter", "J.K Rowling", 2000, 1));
        when(bookService.books()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books));
    }

    @Test
    void shouldReturnBookWhenSearchedByBookName() throws Exception {

        List<Book> books = Arrays.asList(new Book(1, "Nancy drew", "Carolyn keene", 1988, 1));
        when(bookService.findByBookNameOrAuthorName(any())).thenReturn(books);

        mockMvc.perform(get("/books/find")
                .param("name", "Nancy drew"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books));
    }
    @Test
    void shouldReturnBookWhenSearchedByPartOfBookName() throws Exception {

        List<Book> books = Arrays.asList(new Book(1, "Nancy", "Carolyn keene", 1988, 1));
        when(bookService.findByBookNameOrAuthorName(any())).thenReturn(books);

        mockMvc.perform(get("/books/find")
                        .param("name", "Nancy"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books));
    }
    @Test
    void shouldReturnBookWhenSearchedByAuthorName() throws Exception {

        List<Book> books = Arrays.asList(new Book(1, "Nancy drew", "Carolyn keene", 1988, 1));
        when(bookService.findByBookNameOrAuthorName(any())).thenReturn(books);

        mockMvc.perform(get("/books/find")
                        .param("name", "Carolyn keene"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", books));
    }
}
