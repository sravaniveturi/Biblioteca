package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.repository.BookRepository;
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

    public List<Book> books() {

        List<Book> books = new ArrayList<>();
        bookRepository.findAll(Sort.by(Sort.Direction.ASC, "bookName"))
                .forEach(books::add);
        return books;
    }

    public List<Book> findByBookNameOrAuthorName(String name) {
        List<Book> books = bookRepository.findByBookNameContainingIgnoreCase(name);
        bookRepository.findByAuthorNameContainingIgnoreCase(name).forEach(books::add);
        return books;
    }

    public void decrementBookCopyByOne(List<Book> booksCheckedOut) {

        for (Book book: booksCheckedOut) {
            bookRepository.updateNoOfCopies(book.getId());
        }


    }
}
