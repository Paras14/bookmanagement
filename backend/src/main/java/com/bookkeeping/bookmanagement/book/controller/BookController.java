package com.bookkeeping.bookmanagement.book.controller;

import com.bookkeeping.bookmanagement.book.dtos.BookDTO;
import com.bookkeeping.bookmanagement.book.dtos.UserBookDTO;
import com.bookkeeping.bookmanagement.book.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookDTO>> getAllBooks(){
        List<BookDTO> allBooks = bookService.getAllBooks();
        return ResponseEntity.ok(allBooks);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<UserBookDTO>> getUserBooks(Authentication authentication){
        String username = authentication.getName();
        List<UserBookDTO> userBooks = bookService.getUserBooks(username);

        return ResponseEntity.ok(userBooks);
    }

    @GetMapping("/{isbn}")
     public ResponseEntity<UserBookDTO> getUserBookByIsbn(@PathVariable String isbn, Authentication authentication) {
        String username = authentication.getName();
        return bookService.getUserBooksByIsbn(isbn, username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/admin/{isbn}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn){
        bookService.deleteBook(isbn);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{isbn}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removeBookFromUser(@PathVariable String isbn, Authentication authentication){
        String username = authentication.getName();
        bookService.removeBookFromUser(isbn, username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserBookDTO> addBookToUser(@Valid @RequestBody BookDTO bookDTO, Authentication authentication) {
        String username = authentication.getName();
        UserBookDTO userBookDTO = bookService.addBookToUser(bookDTO, username);
//
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                .path("/{isbn}")
//                .buildAndExpand(userBook.getBook().getIsbn())
//                .toUri();

        return ResponseEntity.ok(userBookDTO);
    }

    @PutMapping("/{isbn}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserBookDTO> editBookReadStatus(
            @PathVariable String isbn,
            @RequestBody UserBookDTO userBookDTO,
            Authentication authentication) {
        String username = authentication.getName();
        return bookService.updateReadStatus(isbn, username, userBookDTO.isReadStatus())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
