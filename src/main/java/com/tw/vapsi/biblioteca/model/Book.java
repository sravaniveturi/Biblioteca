package com.tw.vapsi.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String bookName;

    private String authorName;

    private int yearOfPublication;

    private  int  numOfCopies;

    public Book(String bookName, String authorName, int yearOfPublication, int numOfCopies) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.yearOfPublication = yearOfPublication;
        this.numOfCopies = numOfCopies;
    }
}
