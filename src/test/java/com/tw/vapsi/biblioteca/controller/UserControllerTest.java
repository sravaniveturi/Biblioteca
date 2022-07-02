package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.controller.helper.ControllerTestHelper;
import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest extends ControllerTestHelper {
    @Autowired
    private MockMvc mockMvc;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    private List<Book> books;

    @BeforeEach
    void setUp() {
        email = "test-mail@test.com";
        firstName = "Micky";
        lastName = "Mouse";
        password = "password@123";
        books = Arrays.asList(new Book(1, "Harry Potter", "J.K Rowling", 2000, 1));
    }

    @Test
    void shouldCreateAUserWithTheProvidedDetails() throws Exception {
        User user = new User(1L, firstName, lastName, email, password);
        when(userService.save(firstName, lastName, email, password))
                .thenReturn(user);

        mockMvc.perform(post("/users")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("email", email)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void shouldNotCreateUserWhenFirstNameIsMissing() throws Exception {

        mockMvc.perform(post("/users")
                        .param("lastName", lastName)
                        .param("email", email)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status()
                        .reason(createReasonFor("firstName"))
                );
        verify(userService, never()).save(anyString(), anyString(), anyString(), anyString());
    }


    @Test
    void shouldNotCreateUserWhenLastNameIsMissing() throws Exception {

        mockMvc.perform(post("/users")
                        .param("firstName", firstName)
                        .param("email", email)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status()
                        .reason(createReasonFor("lastName"))
                );
        verify(userService, never()).save(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void shouldNotCreateUserWhenEmailIsMissing() throws Exception {

        mockMvc.perform(post("/users")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status()
                        .reason(createReasonFor("email"))
                );
        verify(userService, never()).save(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void shouldNotCreateUserWhenPasswordIsMissing() throws Exception {

        mockMvc.perform(post("/users")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status()
                        .reason(createReasonFor("password"))
                );
        verify(userService, never()).save(anyString(), anyString(), anyString(), anyString());
    }

    private String createReasonFor(String parameterName) {
        return "Required request parameter '" + parameterName + "' for method parameter type String is not present";
    }
    /*@Test
    void shouldAbleToCheckedOutBooks() throws Exception{

        User user = mock(User.class);
        when(userService.checkOut(user)).thenReturn(books);

        mockMvc.perform(post("/users/checkout").sessionAttr("user", user))
                .andExpect(status().isOk());


        verify(userService, times(1)).checkOut(any());

    }*/


    @Test
    void shouldReturnCheckoutBooksForUser() throws Exception {
        User user = new User(1L, firstName, lastName, email, password);

        when(userService.getCheckOutBooks(any())).thenReturn(books);

        mockMvc.perform(get("/users/viewCheckout").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(view().name("viewcheckoutbooks"))
                .andExpect(model().attributeExists("books"));

    }

    @Test
    void shouldReturnNoBooksForUserWithNoCheckOutBooks() throws Exception {
        User user = new User(1L, firstName, lastName, email, password);

        List<Book> books= Lists.newArrayList();
        when(userService.getCheckOutBooks(any())).thenReturn(books);

        mockMvc.perform(get("/users/viewCheckout").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(view().name("viewcheckoutbooks"))
                .andExpect(model().attributeExists("errorMessage"));

    }
}