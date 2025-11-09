package com.bookkeeping.bookmanagement.book.service;

import com.bookkeeping.bookmanagement.book.dtos.BookDTO;
import com.bookkeeping.bookmanagement.book.dtos.UserBookDTO;
import com.bookkeeping.bookmanagement.book.model.Book;
import com.bookkeeping.bookmanagement.book.model.UserBook;
import com.bookkeeping.bookmanagement.book.model.UserBookId;
import com.bookkeeping.bookmanagement.book.model.Users;
import com.bookkeeping.bookmanagement.book.repository.BookRepository;
import com.bookkeeping.bookmanagement.book.repository.UserBookRepository;
import com.bookkeeping.bookmanagement.book.repository.UserRepository;
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

        Book book;
        if (bookDTO.getId() != null) {
            book = bookRepository.findById(bookDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        } else {
            book = new Book();
            book.setBookName(bookDTO.getBookName());
            book.setAuthorName(bookDTO.getAuthorName());
            book.setGenre(bookDTO.getGenre());
            book = bookRepository.save(book);
        }

        if (userBookRepository.findByUserIdAndBookId(user.getId(), book.getId()).isPresent()){
            throw new IllegalStateException("User already owns this book");
        }

        UserBook userBook = new UserBook();
        userBook.setId(new UserBookId(user.getId(), book.getId()));
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

    public Optional<UserBookDTO> getUserBookById(Long bookId, String username) {
        Users user = getUserByUsername(username);
        return userBookRepository.findByUserIdAndBookId(user.getId(), bookId)
                .map(this::convertToDTO);
    }

    public Optional<UserBookDTO> updateReadStatus(Long bookId, String username, boolean readStatus) {
        Users user = getUserByUsername(username);
        return userBookRepository.findByUserIdAndBookId(user.getId(), bookId)
                .map(userBook -> {
                    userBook.setReadStatus(readStatus);
                    return convertToDTO(userBookRepository.save(userBook));
                });
    }

    public void removeBookFromUser(Long bookId, String username){
        Users user = getUserByUsername(username);
        userBookRepository.findByUserIdAndBookId(user.getId(), bookId)
                .ifPresent(userBook -> {
                    userBookRepository.delete(userBook);
                    if (!userBookRepository.existsByBookId(bookId)){
                        bookRepository.deleteById(bookId);
                    }
                });
    }

    public void deleteBook(Long bookId){
        bookRepository.findById(bookId).ifPresent(book -> {
            userBookRepository.deleteByBookId(bookId);
            bookRepository.delete(book);
        });
    }

    private Users getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private UserBookDTO convertToDTO(UserBook userBook) {
        UserBookDTO dto = new UserBookDTO();
        dto.setId(userBook.getBook().getId());
        dto.setBookName(userBook.getBook().getBookName());
        dto.setAuthorName(userBook.getBook().getAuthorName());
        dto.setGenre(userBook.getBook().getGenre());
        dto.setReadStatus(userBook.isReadStatus());
        return dto;
    }

    private BookDTO convertToBookDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setBookName(book.getBookName());
        dto.setAuthorName(book.getAuthorName());
        dto.setGenre(book.getGenre());
        return dto;
    }

}
