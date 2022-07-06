package com.tw.vapsi.biblioteca.controller;


import com.tw.vapsi.biblioteca.controller.helper.ControllerTestHelper;
import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.service.BookService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = BooksController.class)
class BooksControllerTest extends ControllerTestHelper {
    @MockBean
    BookService bookService;

    List<Book> books;
    @Autowired
    private MockMvc mockMvc;

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

        mockMvc.perform(get("/books").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attribute("message", "Sorry No Books available."));
        verify(bookService, times(1)).books();
    }

    @Test
    void shouldReturnBookWhenSearchedByBookName() throws Exception {

        when(bookService.findByBookNameOrAuthorName(any())).thenReturn(books);

        mockMvc.perform(get("/books")
                        .param("name", "Harry").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books))
                .andExpect(view().name("books"));
    }

    @Test
    void shouldReturnBookWhenSearchedByPartOfBookName() throws Exception {

        when(bookService.findByBookNameOrAuthorName(any())).thenReturn(books);

        mockMvc.perform(get("/books")
                        .param("name", "Harry").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books))
                .andExpect(view().name("books"));
    }

    @Test
    void shouldReturnBookWhenSearchedByAuthorName() throws Exception {
        when(bookService.books()).thenReturn(books);
        when(bookService.findByBookNameOrAuthorName(any())).thenReturn(books);

        mockMvc.perform(get("/books")
                        .param("name", "Rowling").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books))
                .andExpect(view().name("books"));
    }

    @Test
    void shouldReturnEmptyWhenSearchNotFound() throws Exception {

        when(bookService.findByBookNameOrAuthorName(any())).thenReturn(Lists.newArrayList());

        mockMvc.perform(get("/books")
                        .param("name", "Rowling").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Sorry No Books available."))
                .andExpect(view().name("books"));
    }

    @Test
    void shouldAbleToCheckedOutBooks() throws Exception {
        List<Book> books = Lists.newArrayList();
        User user = mock(User.class);
        when(bookService.updateCopies(books)).thenReturn(true);
        when(userService.checkOut(anyList(), any())).thenReturn(books);


        mockMvc.perform(post("/checkout").with(user("user")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/viewCheckout"));

    }

    @Test
    void shouldViewCheckoutBooksForUser() throws Exception {
        User user = mock(User.class);
        when(userService.getCheckOutBooks(any())).thenReturn(books);

        mockMvc.perform(get("/viewCheckout").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(view().name("viewcheckoutbooks"))
                .andExpect(model().attributeExists("books"));

    }

    @Test
    void shouldReturnNoBooksForUserWithNoCheckOutBooks() throws Exception {
        User user = mock(User.class);
        List<Book> books = Lists.newArrayList();
        when(userService.getCheckOutBooks(any())).thenReturn(books);

        mockMvc.perform(get("/viewCheckout").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(view().name("viewcheckoutbooks"))
                .andExpect(model().attributeExists("errorMessage"));

    }

    @Test
    void shouldAbleToReturnCheckedOutBooks() throws Exception {
        User user = new User(1L, "Micky", "Mouse", "test-mail@test.com", "password@123");
        when(bookService.returnBooks(any())).thenReturn("1 book returned successfully .");
        mockMvc.perform(post("/return").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .sessionAttr("user", user).with(user("userDetails")))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "1 book returned successfully ."))
                .andExpect(redirectedUrl("/viewCheckout"));
        verify(bookService, times(1)).returnBooks(any());

    }


}