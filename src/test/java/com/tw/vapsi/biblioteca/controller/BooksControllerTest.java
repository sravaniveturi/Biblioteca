package com.tw.vapsi.biblioteca.controller;


import com.tw.vapsi.biblioteca.controller.helper.ControllerTestHelper;
import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

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
    void shouldReturnListOfBooks() throws Exception {

        mockMvc.perform(get("/books"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void shouldReturnBookWhenSearchedByBookName() throws Exception {

        Book book = new Book(1, "Nancy drew", "Carolyn keene", new Date(), 1);
        when(bookService.findByBookNameOrAuthorName(any())).thenReturn(book);

        mockMvc.perform(get("/books/find")
                .param("name", "Nancy drew"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", book));
    }

    @Test
    void shouldReturnBookWhenSearchedByAuthorName() throws Exception {

        Book book = new Book(1, "Nancy drew", "Carolyn keene", new Date(), 1);
        when(bookService.findByBookNameOrAuthorName(any())).thenReturn(book);

        mockMvc.perform(get("/books/find")
                        .param("name", "Carolyn keene"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", book));
    }
}
