package com.tw.vapsi.biblioteca.dataseed;


import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... args) {
        loadBookData();
    }

    private void loadBookData() {
        if (bookRepository.count() == 0) {
            Book user1 = Book.builder().name("Rich Dad Poor Dad").build();
            Book user2 = Book.builder().name("Rich Dad Fundamentals: The CASHFLOW Quadrant").build();
            bookRepository.save(user1);
            bookRepository.save(user2);
        }
    }
}
