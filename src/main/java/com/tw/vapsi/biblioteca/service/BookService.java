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

    public String returnBooks(User user) {
        User userDetailsFromDataBase = getUserDetailsFromDataBaseByEmail(user);
        userDetailsFromDataBase.returnBooks(user);
        int noOfBooksReturned = user.getNoOfBooksReturned();
        userRepository.save(userDetailsFromDataBase);
        String successMessage = getSuccessMessage(noOfBooksReturned);
        return successMessage;

    }


    private String getSuccessMessage(int noOfBooksReturned) {
        String noOfBooksReturnedMsg = noOfBooksReturned + " books returned successfully .";
        if (noOfBooksReturned == 1) {
            noOfBooksReturnedMsg = "1 book returned successfully .";
        }
        return noOfBooksReturnedMsg;
    }



    private User getUserDetailsFromDataBaseByEmail(User user) {
        User userDetailsFromDataBase = userRepository.findByEmail(user.getEmail()).get();
        return userDetailsFromDataBase;
    }


}

