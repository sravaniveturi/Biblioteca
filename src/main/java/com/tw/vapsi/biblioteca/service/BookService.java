package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.repository.BookRepository;
import com.tw.vapsi.biblioteca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    public List<Book> books() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll(Sort.by(Sort.Direction.ASC, "bookName"))
                .forEach(books::add);
        return books;
    }

    public List<Book> findByBookNameOrAuthorName(String name) {
        List<Book> books = new ArrayList<>();
        if(!name.trim().isEmpty()){
            books = bookRepository.findByBookNameContainingIgnoreCase(name);
            bookRepository.findByAuthorNameContainingIgnoreCase(name).forEach(books::add);
        }
      return books;
    }


    public boolean updateCopies(List<Book> checkoutBooks) {
        try {
            for (Book book : checkoutBooks) {
                book.decrementCopies();
            }
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
    public void returnBooks(User user){
        User userDetailsFromDataBase = userRepository.findByEmail(user.getEmail()).get();
        userDetailsFromDataBase.returnBooks(user);
        User userUpdatedWithCheckoutBooks = userRepository.save(userDetailsFromDataBase);

    }


}

