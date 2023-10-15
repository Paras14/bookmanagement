package com.bookkeeping.bookmanagement.Bookpackage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("books/{isbn}")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn){
        boolean isDeleted = bookService.deleteByIsbn(isbn);
        if(isDeleted){
            return new ResponseEntity<>("Book with ISBN " + isbn + " was deleted.", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Book with ISBN " + isbn + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book){
        return bookService.addBook(book.getIsbn(), book.getBookName(),
                book.getAuthorName(), book.getGenre(), false);
    }


}