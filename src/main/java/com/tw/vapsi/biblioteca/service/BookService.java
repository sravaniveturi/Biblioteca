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

    public List<Book> checkOut(List<Book> checkoutBooks, String email) throws Exception {
        User user = userRepository.findByEmail(email).get();
        List<Book> userCheckoutBooks= user.getCheckoutBooks();
        for(Book book: checkoutBooks){
            if(userCheckoutBooks.contains(book)){
              throw new Exception("User has already checkout the book: "+ book.getBookName());
            }
            book.decrementCopies();
            user.addCheckoutBook(book);
        }
        userRepository.save(user);
        return user.getCheckoutBooks();
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

