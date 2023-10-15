package com.bookkeeping.bookmanagement.Bookpackage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<Book> getAllBooks(){
        return bookService.retrieveAllBooks();
    }

    @GetMapping("books/{isbn}")
    public Book retrieveBook(@PathVariable String isbn){
        return bookService.findByIsbn(isbn);
    }

}