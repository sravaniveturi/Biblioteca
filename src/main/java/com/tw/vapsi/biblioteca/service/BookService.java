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

    public Book findByBookNameOrAuthorName(String name) {
        Optional<Book> bookOptional = bookRepository.findByBookName(name);
        if (bookOptional.isPresent()) {
            return bookOptional.get();
        }
        if (bookRepository.findByAuthorName(name).isPresent()) {
            return bookRepository.findByAuthorName(name).get();
        }
        return new Book();
    }
}
