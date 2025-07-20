package com.bookkeeping.bookmanagement.book.dtos;

import com.bookkeeping.bookmanagement.book.model.Book;
import lombok.Data;

@Data
public class UserBookDTO {
    private String isbn;
    private String bookName;
    private String authorName;
    private Book.Genre genre;
    private boolean readStatus;
}
