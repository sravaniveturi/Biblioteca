package com.tw.vapsi.biblioteca.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @ManyToMany
    @JoinTable(name = "users_books",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "checkout_books_id") })
    private List<Book> checkoutBooks = new ArrayList<>();

    public User(long id, String firstName, String lastName, String email, String password) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User() {

    }

    public User(String firstName, String lastName, String email, String password) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public List<Book> getCheckoutBooks() {
        return checkoutBooks;
    }

    public void setCheckoutBooks(List<Book> checkoutBooks) {
        this.checkoutBooks = checkoutBooks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        boolean areIdsEquals = id == user.id;
        boolean isFirstNameEqual = firstName.equals(user.firstName);
        boolean isLastNameEqual = lastName.equals(user.lastName);
        boolean isEmailEqual = email.equals(user.email);
        boolean isPasswordEqual = password.equals(user.password);
        return areIdsEquals && isFirstNameEqual && isLastNameEqual && isEmailEqual && isPasswordEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password);
    }

    public  void addCheckoutBook(Book book)  {
        checkoutBooks.add(book);
    }

    public void returnBooks(User user) {
        for (Book book : user.checkoutBooks) {
            this.checkoutBooks.remove(book);
        }
    }

    public int getNoOfBooksReturned() {
        int noOfBooksReturned = this.getCheckoutBooks().size();
        return noOfBooksReturned;
    }
}
