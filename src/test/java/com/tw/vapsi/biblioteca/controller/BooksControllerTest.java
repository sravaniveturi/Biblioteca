package com.tw.vapsi.biblioteca.controller;


import com.tw.vapsi.biblioteca.controller.helper.ControllerTestHelper;
import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.service.BookService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
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
    List<Book> books;

    @BeforeEach
    void setUp() {
        books = Arrays.asList(new Book(1, "Harry Potter", "J.K Rowling", 2000, 1));
    }

    @Test
    @WithMockUser
    void shouldReturnListOfBooksIfAvailable() throws Exception {
        when(bookService.books()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books));
    }

    @Test
    void shouldReturnBookWhenSearchedByBookName() throws Exception {

        when(bookService.findByBookName(any())).thenReturn(books);

        mockMvc.perform(get("/books/findbook")
                        .param("name", "Nancy drew"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books))
                .andExpect(view().name("bookdetails"));
    }

    @Test
    void shouldReturnBookWhenSearchedByPartOfBookName() throws Exception {

        when(bookService.findByBookName(any())).thenReturn(books);

        mockMvc.perform(get("/books/findbook")
                        .param("name", "Nancy"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books))
                .andExpect(view().name("bookdetails"));
    }

    @Test
    void shouldReturnBookWhenSearchedByAuthorName() throws Exception {

        when(bookService.findByAuthorName(any())).thenReturn(books);

        mockMvc.perform(get("/books/findbyauthor")
                        .param("name", "Carolyn keene"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books))
                .andExpect(view().name("bookdetails"));
    }

    @Test
    void shouldReturnNoBooksFoundWhenSearchNotFound() throws Exception {

        when(bookService.findByAuthorName(any())).thenReturn(Lists.newArrayList());

        mockMvc.perform(get("/books/findbyauthor")
                        .param("name", "Carolyn"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", Lists.newArrayList()))
                .andExpect(view().name("bookdetails"));
    }
}
