package com.bookkeeping.bookmanagement.Bookpackage;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

//    @GetMapping("/books")
//    public List<Book> getAllBooks(){
//
//    }

}