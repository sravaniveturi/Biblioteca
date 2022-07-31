package com.tw.vapasi.biblioteca.service;

import com.tw.vapasi.biblioteca.exceptions.UserNotLoginException;
import com.tw.vapasi.biblioteca.model.Book;
import com.tw.vapasi.biblioteca.model.User;
import com.tw.vapasi.biblioteca.repository.UserRepository;
import com.tw.vapasi.biblioteca.service.dto.UserDetailsDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(UserDetailsDTO::create)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user exists with username : %s", username)));
    }

    public User save(String firstName, String lastName, String email, String password) {
        String encodePassword = bCryptPasswordEncoder.encode(password);
        User user = new User(firstName, lastName, email, encodePassword);
        return userRepository.save(user);
    }

    public List<Book> getCheckOutBooks(String email) throws UserNotLoginException {
        User user = getCurrentUser(email);
        return user.getCheckoutBooks();
    }

    public User getCurrentUser(String email) throws UserNotLoginException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new UserNotLoginException();
        }
        return userOptional.get();
    }
}
