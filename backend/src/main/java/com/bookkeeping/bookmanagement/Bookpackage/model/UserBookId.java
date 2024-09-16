package com.bookkeeping.bookmanagement.Bookpackage.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserBookId implements Serializable {

    private Long userId;
    private String bookIsbn;

    public UserBookId() {
    }

    public UserBookId(Long userId, String bookIsbn){
        this.userId = userId;
        this.bookIsbn = bookIsbn;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof UserBookId)) return false;
        UserBookId that = (UserBookId)  o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getBookIsbn(), that.getBookIsbn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getBookIsbn());
    }
}
