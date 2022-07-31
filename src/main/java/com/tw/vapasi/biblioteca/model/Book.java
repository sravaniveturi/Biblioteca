package com.tw.vapasi.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    private int numOfCopies;

    public void decrementCopies() {
        if(this.numOfCopies > 0) {
            this.numOfCopies--;
        }
    }
    public void incrementCopies()  {
        this.numOfCopies++;
    }

    public int getNumOfCopies() {
        return numOfCopies;
    }
}
