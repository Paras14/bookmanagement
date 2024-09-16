package com.bookkeeping.bookmanagement.Bookpackage.service;

import com.bookkeeping.bookmanagement.Bookpackage.dtos.BookDTO;
import com.bookkeeping.bookmanagement.Bookpackage.dtos.UserBookDTO;
import com.bookkeeping.bookmanagement.Bookpackage.model.Book;
import com.bookkeeping.bookmanagement.Bookpackage.model.UserBook;
import com.bookkeeping.bookmanagement.Bookpackage.model.UserBookId;
import com.bookkeeping.bookmanagement.Bookpackage.model.Users;
import com.bookkeeping.bookmanagement.Bookpackage.repository.BookRepository;
import com.bookkeeping.bookmanagement.Bookpackage.repository.UserBookRepository;
import com.bookkeeping.bookmanagement.Bookpackage.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;

    public BookService(BookRepository bookRepository, UserRepository userRepository,
                       UserBookRepository userBookRepository){
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userBookRepository = userBookRepository;
    }

    public UserBookDTO addBookToUser(BookDTO bookDTO, String username){
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Book book = bookRepository.findById(bookDTO.getIsbn())
                .orElseGet(() -> {
                    Book newBook = new Book();
                    newBook.setIsbn(bookDTO.getIsbn());
                    newBook.setBookName(bookDTO.getBookName());
                    newBook.setAuthorName(bookDTO.getAuthorName());
                    newBook.setGenre(bookDTO.getGenre());
                    return bookRepository.save(newBook);
                });

        UserBook userBook = new UserBook();
        userBook.setId(new UserBookId(user.getId(), book.getIsbn()));
        userBook.setUser(user);
        userBook.setBook(book);
        userBook.setReadStatus(false);

        UserBook savedUserBook = userBookRepository.save(userBook);
        return convertToDTO(savedUserBook);
    }

    public List<UserBookDTO> getUserBooks(String username) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<UserBook> userBooks = userBookRepository.findByUserId(user.getId());
        return userBooks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream().map(this::convertToBookDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserBookDTO> getUserBooksByIsbn(String isbn, String username) {
        Users user = getUserByUsername(username);
        return userBookRepository.findByUserIdAndBookIsbn(user.getId(), isbn)
                .map(this::convertToDTO);
    }

    public Optional<UserBookDTO> updateReadStatus(String isbn, String username, boolean readStatus) {
        Users user = getUserByUsername(username);
        return userBookRepository.findByUserIdAndBookIsbn(user.getId(), isbn)
                .map(userBook -> {
                    userBook.setReadStatus(readStatus);
                    return convertToDTO(userBookRepository.save(userBook));
                });
    }

    public void removeBookFromUser(String isbn, String username){
        Users user = getUserByUsername(username);
        userBookRepository.findByUserIdAndBookIsbn(user.getId(), isbn)
                .ifPresent(userBook -> {
                    userBookRepository.delete(userBook);
                    if (!userBookRepository.existsByBookIsbn(isbn)){
                        bookRepository.deleteById(isbn);
                    }
                });
    }

    public void deleteBook(String isbn){
        bookRepository.findById(isbn).ifPresent(book -> {
            userBookRepository.deleteByBookIsbn(isbn);
            bookRepository.delete(book);
        });
    }

    private Users getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private UserBookDTO convertToDTO(UserBook userBook) {
        UserBookDTO dto = new UserBookDTO();
        dto.setIsbn(userBook.getBook().getIsbn());
        dto.setBookName(userBook.getBook().getBookName());
        dto.setAuthorName(userBook.getBook().getAuthorName());
        dto.setGenre(userBook.getBook().getGenre());
        dto.setReadStatus(userBook.isReadStatus());
        return dto;
    }

    private BookDTO convertToBookDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setIsbn(book.getIsbn());
        dto.setBookName(book.getBookName());
        dto.setAuthorName(book.getAuthorName());
        dto.setGenre(book.getGenre());
        return dto;
    }

}
