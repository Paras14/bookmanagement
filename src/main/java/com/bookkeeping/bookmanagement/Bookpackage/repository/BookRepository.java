package com.bookkeeping.bookmanagement.Bookpackage.repository;

import com.bookkeeping.bookmanagement.Bookpackage.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {

}
