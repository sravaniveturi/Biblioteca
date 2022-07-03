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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    void shouldReturnListOfAvailableBooks() throws Exception {
        when(bookService.books()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", books))
                .andExpect(model().attribute("books", hasSize(1)))
                .andExpect(model().attribute("books", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("bookName", is("Harry Potter")),
                                hasProperty("authorName", is("J.K Rowling")),
                                hasProperty("yearOfPublication", is(2000)),
                                hasProperty("numOfCopies", is(1))
                        )
                )));

        verify(bookService, times(1)).books();
    }

    @Test
    @WithMockUser
    void shouldReturnMessageWhenNoBooksAvailable() throws Exception {
        books = new ArrayList<>();
        when(bookService.books()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attribute("message", "Sorry no books available"));
        verify(bookService, times(1)).books();
    }

    @Test
    void shouldReturnBookWhenSearchedByBookName() throws Exception {

        when(bookService.findByBookNameOrAuthorName(any())).thenReturn(books);

        mockMvc.perform(get("/findbook")
                        .param("name", "Harry"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books))
                .andExpect(view().name("findbook"));
    }

    @Test
    void shouldReturnBookWhenSearchedByPartOfBookName() throws Exception {

        when(bookService.findByBookNameOrAuthorName(any())).thenReturn(books);

        mockMvc.perform(get("/findbook")
                        .param("name", "Harry"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books))
                .andExpect(view().name("findbook"));
    }

    @Test
    void shouldReturnBookWhenSearchedByAuthorName() throws Exception {

        when(bookService.findByBookNameOrAuthorName(any())).thenReturn(books);

        mockMvc.perform(get("/findbook")
                        .param("name", "Rowling"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books))
                .andExpect(view().name("findbook"));
    }

    @Test
    void shouldReturnEmptyWhenSearchNotFound() throws Exception {

        when(bookService.findByBookNameOrAuthorName(any())).thenReturn(Lists.newArrayList());

        mockMvc.perform(get("/findbook")
                        .param("name", "Rowling"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", Lists.newArrayList()))
                .andExpect(view().name("findbook"));
    }
}
