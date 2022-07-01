package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.repository.UserRepository;
import com.tw.vapsi.biblioteca.service.dto.UserDetailsDTO;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserService userService;


    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, bCryptPasswordEncoder);
    }

    @Test
    void shouldLoadUserDetailsFromUserName() {
        User user = new User(1L,
                "User 1 first name",
                "User 1 last name",
                "email@example.com",
                "password");
        UserDetails expectedUserDetails = UserDetailsDTO.create(user);
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("email@example.com");

        assertEquals(expectedUserDetails, userDetails);
    }

    @Test
    void shouldThrowUserNotFoundException() {
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.empty());

        UsernameNotFoundException actualException = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("email@example.com")
        );

        assertEquals("No user exists with username : email@example.com", actualException.getMessage());
    }

    @Test
    void shouldSaveTheUserInformation() {
        User userToBeCreated = new User(
                "Micky",
                "Mouse",
                "micky-mouse@example.com",
                "encoded-password");


        User expectedUser = new User(
                1L,
                "Micky",
                "Mouse",
                "micky-mouse@example.com",
                "encoded-password");
        when(userRepository.save(userToBeCreated)).thenReturn(expectedUser);
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encoded-password");

        User actualUser = userService.save("Micky", "Mouse", "micky-mouse@example.com", "password");

        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).save(userToBeCreated);
    }

    @Test
    void shouldReturnBooksCheckedOutForUser() {
        User user = new User(
                "Micky",
                "Mouse",
                "micky-mouse@example.com",
                "encoded-password");
        List<Book> books = Arrays.asList(new Book(1, "Harry Potter", "J.K Rowling", 2000, 1));
        user.setCheckoutBooks(books);
        when(userRepository.save(any())).thenReturn(user);

        List<Book> booksCheckedOut=userService.checkOut(user);

        assertEquals(books,booksCheckedOut);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldReturnEmptyForUserWithNoCheckOutBooks() {
        List<Book> expectedBooks = Lists.newArrayList();
        User user = new User(
                1L,
                "Micky",
                "Mouse",
                "micky-mouse@example.com",
                "encoded-password");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<Book> booksReturned = userService.getCheckOutBooks(1);

        assertTrue(booksReturned.isEmpty());
        verify(userRepository, times(1)).findById(1L);
    }
    @Test
    void shouldReturnCheckoutBooksForUser() {
        List<Book> expectedBooks = Arrays.asList(new Book(1, "abc","abc",1,12));
        User user = new User(
                1L,
                "Micky",
                "Mouse",
                "micky-mouse@example.com",
                "encoded-password");
        user.setCheckoutBooks(expectedBooks);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<Book> booksReturned = userService.getCheckOutBooks(1);

        assertEquals(booksReturned, expectedBooks);
        verify(userRepository, times(1)).findById(1L);
    }




}