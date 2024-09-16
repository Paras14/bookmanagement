package com.bookkeeping.bookmanagement.Bookpackage.dtos;

import com.bookkeeping.bookmanagement.Bookpackage.model.Book;
import lombok.Data;

@Data
public class UserBookDTO {
    private String isbn;
    private String bookName;
    private String authorName;
    private Book.Genre genre;
    private boolean readStatus;
}
