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
        //to be removed when db created
        Book book1 = new Book(1, "The Diary of a Young Girl","Anne Frank",2014,2);
        bookRepository.save(book1);
        Book book2 = new Book(2, "A Gentleman in Moscow","Amor Towles ",2017,2);
        bookRepository.save(book2);

        List<Book> books = new ArrayList<>();
        bookRepository.findAll(Sort.by(Sort.Direction.ASC, "bookName"))
                .forEach(books::add);
        return books;
    }

    public List<Book> findByBookName(String name) {
        List<Book> books = bookRepository.findByBookNameContainingIgnoreCase(name);
        return books;
    }
    public List<Book> findByAuthorName(String name) {
        List<Book> books = bookRepository.findByAuthorNameContainingIgnoreCase(name);
        return books;
    }

}
