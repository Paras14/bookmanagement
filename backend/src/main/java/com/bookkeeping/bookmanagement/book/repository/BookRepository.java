package com.bookkeeping.bookmanagement.book.repository;

import com.bookkeeping.bookmanagement.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {

}
