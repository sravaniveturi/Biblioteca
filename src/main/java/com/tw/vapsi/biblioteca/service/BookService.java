package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public Book findByBookNameOrAuthorName(String name) {
        Optional<Book> bookOptional = bookRepository.findByBookName(name);
        if(bookOptional.isPresent()){
           return bookOptional.get();
        }
        if(bookRepository.findByAuthorName(name).isPresent()){
            return bookRepository.findByAuthorName(name).get();
        }
        return new Book();
    }
}
