package com.bookkeeping.bookmanagement.Bookpackage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

//@RestController
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> books = bookService.retrieveAllBooks();
        if(books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("books/{isbn}")
    public ResponseEntity<Book> retrieveBook(@PathVariable String isbn){
        Optional<Book> book = bookService.findByIsbn(isbn);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
    public ResponseEntity<Book> addBook(@RequestBody Book book){
        try{
            Book createdBook = bookService.addBook(book.getIsbn(), book.getBookName(),
                    book.getAuthorName(), book.getGenre(), false);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{path}")
                    .buildAndExpand(createdBook.getIsbn())
                    .toUri();
            return ResponseEntity.created(location).body(createdBook);
        } catch (IllegalStateException e){
            //Handle exception in case the book already exists
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


}