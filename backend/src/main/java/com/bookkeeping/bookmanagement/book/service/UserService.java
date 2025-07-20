package com.bookkeeping.bookmanagement.book.service;

import com.bookkeeping.bookmanagement.book.model.Book;
import com.bookkeeping.bookmanagement.book.model.UserBook;
import com.bookkeeping.bookmanagement.book.model.Users;
import com.bookkeeping.bookmanagement.book.repository.BookRepository;
import com.bookkeeping.bookmanagement.book.repository.UserBookRepository;
import com.bookkeeping.bookmanagement.book.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;

    public UserService(UserRepository userRepository, BookRepository bookRepository,
                       UserBookRepository userBookRepository){
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.userBookRepository = userBookRepository;
    }

    //User login related function
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public Users getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Users register(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

//    public Set<UserBook> getUserBooks(String username){
//        Users user = this.getUserByUsername(username);
//        return user.getUserBooks();
//    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<UserBook> getUserBookByIsbn(String isbn, String username){
        Users user = this.getUserByUsername(username);
        return  userBookRepository.findByUserIdAndBookIsbn(user.getId(), isbn);
    }

    public UserBook addBookToUser(Book book, String username) {
        Users user = getUserByUsername(username);

        Optional<Book> existingBook = bookRepository.findById(book.getIsbn());
        Book savedBook = existingBook.orElseGet(() -> bookRepository.save(book));

        Optional<UserBook> userBookOptional = userBookRepository.findByUserIdAndBookIsbn(user.getId(), savedBook.getIsbn());
        if (userBookOptional.isPresent()){
            throw new IllegalStateException("User already owns this book");
        }

        UserBook userBook = new UserBook(user, savedBook, false);
        return userBookRepository.save(userBook);
    }

    public UserBook updateReadStatus(String isbn, String username, boolean readStatus) {
        Users user = this.getUserByUsername(username);
        Optional<UserBook> userBookOptional = userBookRepository.findByUserIdAndBookIsbn(user.getId(), isbn);

        if (userBookOptional.isPresent()) {
            UserBook userBook = userBookOptional.get();
            userBook.setReadStatus(readStatus);
            return  userBookRepository.save(userBook);
        } else {
            throw new IllegalStateException("User does not own this book");
        }
    }

    //admin only
    public void deleteBook(String isbn) {
        if(bookRepository.existsById(isbn)){
            userBookRepository.deleteByBookIsbn(isbn);
            bookRepository.deleteById(isbn);
        } else {
            throw new IllegalStateException("Book not found");
        }
    }

    public void removeBookFromUser(String isbn, String username) {
        Users user = getUserByUsername(username);
        Optional<UserBook> userBookOptional = userBookRepository
                .findByUserIdAndBookIsbn(user.getId(), isbn);

        if (userBookOptional.isPresent()){
            UserBook userBook = userBookOptional.get();
            userBookRepository.delete(userBook);

            boolean isBookOwnedByAnyUser = userBookRepository.existsByBookIsbn(isbn);
            if (!isBookOwnedByAnyUser) {
                bookRepository.deleteById(isbn);
            }
        } else {
            throw new IllegalStateException("User does not own this book");
        }
    }
}
