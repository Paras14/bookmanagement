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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock private BookRepository bookRepository;
    @Mock private UserRepository userRepository;
    @Mock private UserBookRepository userBookRepository;

    @InjectMocks private BookService bookService;

    private Users user;
    private Book book;
    private UserBook userBook;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(1L);
        user.setUsername("john");

        book = new Book();
        book.setIsbn("isbn-12345");
        book.setBookName("BookName");
        book.setAuthorName("Author");
        book.setGenre(Book.Genre.valueOf("FANTASY"));

        userBook = new UserBook();
        userBook.setId(new UserBookId(user.getId(), book.getIsbn()));
        userBook.setUser(user);
        userBook.setBook(book);
        userBook.setReadStatus(false);

        bookDTO = new BookDTO();
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setBookName(book.getBookName());
        bookDTO.setAuthorName(book.getAuthorName());
        bookDTO.setGenre(book.getGenre());
    }

    @Test
    void addBookToUser_existingBook_success() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(bookRepository.findById("isbn-12345")).thenReturn(Optional.of(book));
        when(userBookRepository.save(any(UserBook.class))).thenReturn(userBook);

        UserBookDTO result = bookService.addBookToUser(bookDTO, "john");

        assertThat(result.getIsbn()).isEqualTo(book.getIsbn());
        verify(bookRepository, never()).save(any());
        verify(userBookRepository).save(any(UserBook.class));
    }

    @Test
    void addBookToUser_newBook_success() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(bookRepository.findById("isbn-12345")).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(userBookRepository.save(any(UserBook.class))).thenReturn(userBook);

        UserBookDTO result = bookService.addBookToUser(bookDTO, "john");

        assertThat(result.getBookName()).isEqualTo("BookName");
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void addBookToUser_userNotFound_throws() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.addBookToUser(bookDTO, "john"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void getUserBooks_success() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserId(1L)).thenReturn(List.of(userBook));

        List<UserBookDTO> result = bookService.getUserBooks("john");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    void getUserBooks_userNotFound_throws() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getUserBooks("john"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void getAllBooks_success() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookDTO> result = bookService.getAllBooks();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    void getUserBooksByIsbn_found() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserIdAndBookIsbn(1L, "isbn-1")).thenReturn(Optional.of(userBook));

        Optional<UserBookDTO> result = bookService.getUserBooksByIsbn("isbn-1", "john");

        assertThat(result).isPresent();
        assertThat(result.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    void getUserBooksByIsbn_notFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserIdAndBookIsbn(1L, "isbn-1")).thenReturn(Optional.empty());

        Optional<UserBookDTO> result = bookService.getUserBooksByIsbn("isbn-1", "john");

        assertThat(result).isEmpty();
    }

    @Test
    void updateReadStatus_found() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserIdAndBookIsbn(1L, "isbn-1")).thenReturn(Optional.of(userBook));
        when(userBookRepository.save(any(UserBook.class))).thenReturn(userBook);

        Optional<UserBookDTO> result = bookService.updateReadStatus("isbn-1", "john", true);

        assertThat(result).isPresent();
        verify(userBookRepository).save(argThat(UserBook::isReadStatus));
    }

    @Test
    void updateReadStatus_notFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserIdAndBookIsbn(1L, "isbn-1")).thenReturn(Optional.empty());

        Optional<UserBookDTO> result = bookService.updateReadStatus("isbn-1", "john", true);

        assertThat(result).isEmpty();
    }

    @Test
    void removeBookFromUser_deletesBookAndUserBook() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserIdAndBookIsbn(1L, "isbn-1")).thenReturn(Optional.of(userBook));
        when(userBookRepository.existsByBookIsbn("isbn-1")).thenReturn(false);

        bookService.removeBookFromUser("isbn-1", "john");

        verify(userBookRepository).delete(userBook);
        verify(bookRepository).deleteById("isbn-1");
    }

    @Test
    void removeBookFromUser_keepsBookIfExistsOtherUser() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserIdAndBookIsbn(1L, "isbn-1")).thenReturn(Optional.of(userBook));
        when(userBookRepository.existsByBookIsbn("isbn-1")).thenReturn(true);

        bookService.removeBookFromUser("isbn-1", "john");

        verify(userBookRepository).delete(userBook);
        verify(bookRepository, never()).deleteById(anyString());
    }

    @Test
    void removeBookFromUser_noUserBook_nothingHappens() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserIdAndBookIsbn(1L, "isbn-1")).thenReturn(Optional.empty());

        bookService.removeBookFromUser("isbn-1", "john");

        verify(userBookRepository, never()).delete(any());
        verify(bookRepository, never()).deleteById(anyString());
    }

    @Test
    void deleteBook_exists_deletesBookAndUserBooks() {
        when(bookRepository.findById("isbn-1")).thenReturn(Optional.of(book));

        bookService.deleteBook("isbn-1");

        verify(userBookRepository).deleteByBookIsbn("isbn-1");
        verify(bookRepository).delete(book);
    }

    @Test
    void deleteBook_notExists_doesNothing() {
        when(bookRepository.findById("isbn-1")).thenReturn(Optional.empty());

        bookService.deleteBook("isbn-1");

        verify(userBookRepository, never()).deleteByBookIsbn(anyString());
        verify(bookRepository, never()).delete(any(Book.class));
    }
}
