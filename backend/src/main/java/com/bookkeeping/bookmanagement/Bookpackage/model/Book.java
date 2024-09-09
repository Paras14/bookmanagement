package com.bookkeeping.bookmanagement.Bookpackage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Book {

    @Id
    @Size(max = 14, min = 10, message = "Enter a Valid ISBN")
    @Column(unique = true)
    private String isbn;
    @NotBlank(message = "Book name can't be empty")
    private String bookName;
    @NotBlank(message = "Author name can't be empty")
    private String authorName;
    @Enumerated(EnumType.STRING)
    private Genre genre;

    private boolean readStatus;

    @ManyToMany(mappedBy = "books")
    private Set<Users> users = new HashSet<>();

    public Book() {

    }

    public Set<Users> getUsers() {
        return users;
    }

    public void setUsers(Set<Users> users) {
        this.users = users;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Genre getGenre() {
        return genre;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public Book(String isbn, String bookName, String authorName, Genre genre, boolean readStatus) {
        this.isbn = isbn;
        this.bookName = bookName;
        this.authorName = authorName;
        this.genre = genre;
        this.readStatus = readStatus;
    }
    @Override
    public String toString() {
        return "Book{" +
                "isbn=" + isbn +
                ", bookName='" + bookName + '\'' +
                ", authorName='" + authorName + '\'' +
                ", genre=" + genre +
                ", readStatus=" + readStatus +
                '}';
    }

    public enum Genre{
        COMEDY,
        THRILLER,
        SELF_HELP,
        SCIENCE_FICTION,
        MYSTERY,
        ROMANCE,
        FANTASY
    }


}
