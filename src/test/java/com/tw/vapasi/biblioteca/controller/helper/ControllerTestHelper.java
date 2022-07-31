package com.tw.vapasi.biblioteca.controller.helper;

import com.tw.vapasi.biblioteca.service.UserService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class ControllerTestHelper {
    @MockBean
    protected UserService userService;
    @MockBean
    protected BCryptPasswordEncoder bCryptPasswordEncoder;
}
