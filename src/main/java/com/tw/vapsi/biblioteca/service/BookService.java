package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    BookRepository booksRepository;

    public BookService(BookRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> books() {
        List<Book> books= new ArrayList<>();
        booksRepository.findAll().forEach(books :: add);
        return books;
    }
}
