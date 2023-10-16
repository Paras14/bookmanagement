package com.bookkeeping.bookmanagement.Bookpackage;

import com.bookkeeping.bookmanagement.Bookpackage.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class BookJpaController {
//    private BookService bookService;
    private BookRepository bookRepository;

    public BookJpaController(BookRepository bookRepository){
//        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(){
//        List<Book> books = bookService.retrieveAllBooks();
        List<Book> books = bookRepository.findAll();
        if(books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("books/{isbn}")
    public ResponseEntity<Book> retrieveBook(@PathVariable String isbn){
//        Optional<Book> book = bookService.findByIsbn(isbn);
        Optional<Book> book = bookRepository.findById(isbn);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("books/{isbn}")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn){
//        boolean isDeleted = bookService.deleteByIsbn(isbn);
        boolean isExists = bookRepository.existsById(isbn);
        if(isExists){
            bookRepository.deleteById(isbn);
            return new ResponseEntity<>("Book with ISBN " + isbn + " was deleted.", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Book with ISBN " + isbn + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        try {
            Book createdBook = bookRepository.save(book);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{path}")
                    .buildAndExpand(createdBook.getIsbn())
                    .toUri();
            return ResponseEntity.created(location).body(createdBook);
        } catch (IllegalStateException e) {
            //Handle exception in case the book already exists
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }
}
