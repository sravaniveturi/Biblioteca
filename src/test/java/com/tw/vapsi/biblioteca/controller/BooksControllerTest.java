package com.tw.vapsi.biblioteca.controller;


import com.tw.vapsi.biblioteca.controller.helper.ControllerTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = BooksController.class)
class BooksControllerTest extends ControllerTestHelper {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void shouldReturnListOfBooks() throws Exception {

        mockMvc.perform(get("/books"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }
}
