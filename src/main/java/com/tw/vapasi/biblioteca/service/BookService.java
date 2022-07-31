package com.tw.vapasi.biblioteca.service;

import com.tw.vapasi.biblioteca.exceptions.BookAlreadyCheckoutException;
import com.tw.vapasi.biblioteca.exceptions.BookNotAvailableException;
import com.tw.vapasi.biblioteca.exceptions.BooksNotReturnedException;
import com.tw.vapasi.biblioteca.exceptions.UserNotLoginException;
import com.tw.vapasi.biblioteca.model.Book;
import com.tw.vapasi.biblioteca.model.User;
import com.tw.vapasi.biblioteca.repository.BookRepository;
import com.tw.vapasi.biblioteca.repository.UserRepository;
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

    @Autowired
    UserService userService;

    public List<Book> books() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll(Sort.by(Sort.Direction.ASC, "bookName"))
                .forEach(books::add);
        return books;
    }

    public List<Book> findByBookNameOrAuthorName(String name) {
        List<Book> books = new ArrayList<>();
        if (!name.trim().isEmpty()) {
            books = bookRepository.findByBookNameContainingIgnoreCase(name);
            bookRepository.findByAuthorNameContainingIgnoreCase(name).forEach(books::add);
        }
        return books;
    }


    public void checkOut(List<Book> checkoutBooks, String email)
            throws BookAlreadyCheckoutException, UserNotLoginException, BookNotAvailableException {

        User user = userService.getCurrentUser(email);
        addCheckoutBook(user, checkoutBooks);
        userRepository.save(user);
    }

    private void addCheckoutBook(User user, List<Book> checkoutBooks) throws BookAlreadyCheckoutException, BookNotAvailableException {
        List<Book> userCheckoutBooks = user.getCheckoutBooks();
        for (Book book : checkoutBooks) {
            if (book.getNumOfCopies() == 0) {
                throw new BookNotAvailableException(book.getBookName());
            }
            user.addCheckoutBook(book);
            book.decrementCopies();
            bookRepository.save(book);
        }
    }

    public int returnBooks(List<Book> returnBooks, String email) throws BooksNotReturnedException, UserNotLoginException {
        User user = userService.getCurrentUser(email);
        List<Book> userCheckoutBooks = user.getCheckoutBooks();
        int counter = 0;

        for (Book book : returnBooks) {
            user.returnBook(book);
            book.incrementCopies();
            bookRepository.save(book);
            counter++;
        }
        userRepository.save(user);
        return counter;
    }


}

