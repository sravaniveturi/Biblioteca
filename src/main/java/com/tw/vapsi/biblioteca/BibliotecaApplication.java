package com.tw.vapsi.biblioteca;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BibliotecaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApplication.class, args);
	}
	@Bean
	public CommandLineRunner setup(BookRepository repository) {
		return (args) -> {
			repository.save(new Book(1L,"ABC", "ABC" ,1, 2000));
			repository.save(new Book(2L,"DSE", "ABC" ,1, 2000));
			repository.save(new Book(3L,"Love story", "Ram" ,1, 2000));
			repository.save(new Book(4L,"Harry potter", "JK Rowling" ,1, 1997));
		};
	}
}
