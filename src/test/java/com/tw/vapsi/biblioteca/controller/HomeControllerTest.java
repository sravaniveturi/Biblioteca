package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.controller.helper.ControllerTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HomeController.class)
class HomeControllerTest extends ControllerTestHelper {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void shouldShowWelcomeMessage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("welcomeText", "Welcome to Biblioteca."))
                .andExpect(model().attribute("tagLineText", "Your one-stop-shop for great book titles in Bangalore!"));
    }
}
