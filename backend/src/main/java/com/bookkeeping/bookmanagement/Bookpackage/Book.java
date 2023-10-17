package com.bookkeeping.bookmanagement.Bookpackage;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Book {

    @Id
    private String isbn;
    private String bookName;
    private String authorName;
    private Genre genre;

    private boolean readStatus;

    public Book() {

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
