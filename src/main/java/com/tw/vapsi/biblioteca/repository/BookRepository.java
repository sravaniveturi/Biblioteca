package com.tw.vapsi.biblioteca.repository;

import com.tw.vapsi.biblioteca.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByBookName(String bookName);

    Optional<Book> findByAuthorName(String authorName);



}
