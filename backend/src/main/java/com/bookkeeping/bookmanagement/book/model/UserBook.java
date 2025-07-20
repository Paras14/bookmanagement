package com.bookkeeping.bookmanagement.book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBook {

    @EmbeddedId
    private UserBookId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @MapsId("bookIsbn")
    @JoinColumn(name = "book_isbn")
    private Book book;

    @Column(nullable = false)
    private boolean readStatus;

    public UserBook(Users user, Book book, boolean readStatus) {
        this.user = user;
        this.book = book;
        this.readStatus = readStatus;
        this.id = new UserBookId(user.getId(), book.getIsbn());
    }

}
