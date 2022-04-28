package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void shouldCreateAUserWithTheProvidedDetails() throws Exception {
        User user = new User(1L, "Micky", "Mouse", "test-mail@test.com", "password");
        when(userService.save("Micky", "Mouse", "test-mail@test.com", "password"))
                .thenReturn(user);

        mockMvc.perform(post("/users")
                        .param("firstName", "Micky")
                        .param("lastName", "Mouse")
                        .param("email", "test-mail@test.com")
                        .param("password", "password")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Micky"))
                .andExpect(jsonPath("$.lastName").value("Mouse"))
                .andExpect(jsonPath("$.email").value("test-mail@test.com"));
    }

    @Test
    void shouldNotCreateUserWhenFirstNameIsMissing() throws Exception {

        mockMvc.perform(post("/users")
                        .param("lastName", "Mouse")
                        .param("email", "test-mail@test.com")
                        .param("password", "password")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status()
                        .reason("Required request parameter 'firstName' for method parameter type String is not present")
                );
        verify(userService, never()).save(anyString(), anyString(), anyString(), anyString());
    }


    @Test
    void shouldNotCreateUserWhenLastNameIsMissing() throws Exception {

        mockMvc.perform(post("/users")
                        .param("firstName", "Micky")
                        .param("email", "test-mail@test.com")
                        .param("password", "password")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status()
                        .reason("Required request parameter 'lastName' for method parameter type String is not present")
                );
        verify(userService, never()).save(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void shouldNotCreateUserWhenEmailIsMissing() throws Exception {

        mockMvc.perform(post("/users")
                        .param("firstName", "Micky")
                        .param("lastName", "Mouse")
                        .param("password", "password")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status()
                        .reason("Required request parameter 'email' for method parameter type String is not present")
                );
        verify(userService, never()).save(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void shouldNotCreateUserWhenPasswordIsMissing() throws Exception {

        mockMvc.perform(post("/users")
                        .param("firstName", "Micky")
                        .param("lastName", "Mouse")
                        .param("email", "test-mail@test.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status()
                        .reason("Required request parameter 'password' for method parameter type String is not present")
                );
        verify(userService, never()).save(anyString(), anyString(), anyString(), anyString());
    }
}