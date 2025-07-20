package com.bookkeeping.bookmanagement.book.service;

import com.bookkeeping.bookmanagement.book.model.Book;
import com.bookkeeping.bookmanagement.book.model.UserBook;
import com.bookkeeping.bookmanagement.book.model.Users;
import com.bookkeeping.bookmanagement.book.repository.BookRepository;
import com.bookkeeping.bookmanagement.book.repository.UserBookRepository;
import com.bookkeeping.bookmanagement.book.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private BookRepository bookRepository;
    @Mock private UserBookRepository userBookRepository;

    @InjectMocks private UserService userService;

    private Users user;
    private Book book;
    private UserBook userBook;

    @BeforeEach
    void setup() {
        user = new Users();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setRole("USER");

        book = new Book();
        book.setIsbn("isbn-12345");
        book.setBookName("BookName");
        book.setAuthorName("Author");
        book.setGenre(Book.Genre.valueOf("FANTASY"));

        userBook = new UserBook(user, book, false);
    }

    @Test
    void getUserByUsername_userExists() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        Users result = userService.getUserByUsername("testUser");
        assertThat(result).isEqualTo(user);
    }

    @Test
    void getUserByUsername_userNotFound_throws() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getUserByUsername("testUser"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void register_encodesPasswordAndSaves() {
        Users newUser = new Users();
        newUser.setUsername("JohnDoe");
        newUser.setPassword("plainPassword123");
        newUser.setRole("USER");

        ArgumentCaptor<Users> captor = ArgumentCaptor.forClass(Users.class);
        when(userRepository.save(any(Users.class))).thenAnswer(i -> i.getArgument(0));

        Users result = userService.register(newUser); // To trigger user.setPassword(passwordEncoder.encode(user.getPassword()))

        verify(userRepository).save(captor.capture());
        Users savedUser = captor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo("JohnDoe");
        assertThat(savedUser.getPassword()).isNotEqualTo("plainPassword123");
        assertThat(savedUser.getPassword()).matches("^\\$2[aby]\\$.{56}$");
    }

    @Test
    void getAllBooks_returnsList() {
        when(bookRepository.findAll()).thenReturn(List.of(book));
        List<Book> result = userService.getAllBooks();
        assertThat(result).containsExactly(book);
    }

    @Test
    void getUserBookByIsbn_found() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserIdAndBookIsbn(user.getId(), "isbn-12345"))
                .thenReturn(Optional.of(userBook));

        Optional<UserBook> result = userService.getUserBookByIsbn("isbn-12345", "testUser");
        assertThat(result).isPresent().get().isEqualTo(userBook);
    }

    @Test
    void getUserBookByIsbn_notFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserIdAndBookIsbn(user.getId(), "isbn-12345"))
                .thenReturn(Optional.empty());

        Optional<UserBook> result = userService.getUserBookByIsbn("isbn-12345", "testUser");
        assertThat(result).isEmpty();
    }

    @Test
    void addBookToUser_existingBook_newOwnership() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.of(book));
        when(userBookRepository.findByUserIdAndBookIsbn(user.getId(), book.getIsbn()))
                .thenReturn(Optional.empty());
        when(userBookRepository.save(any(UserBook.class))).thenReturn(userBook);

        UserBook result = userService.addBookToUser(book, "testUser");
        assertThat(result).isEqualTo(userBook);
    }

    @Test
    void addBookToUser_newBook() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);
        when(userBookRepository.findByUserIdAndBookIsbn(user.getId(), book.getIsbn()))
                .thenReturn(Optional.empty());
        when(userBookRepository.save(any(UserBook.class))).thenReturn(userBook);

        UserBook result = userService.addBookToUser(book, "testUser");
        assertThat(result).isEqualTo(userBook);
        verify(bookRepository).save(book);
    }

    @Test
    void addBookToUser_alreadyOwned_throws() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.of(book));
        when(userBookRepository.findByUserIdAndBookIsbn(user.getId(), book.getIsbn()))
                .thenReturn(Optional.of(userBook));

        assertThatThrownBy(() -> userService.addBookToUser(book, "testUser"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("User already owns this book");
    }

    @Test
    void updateReadStatus_existingUserBook_success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserIdAndBookIsbn(user.getId(), book.getIsbn()))
                .thenReturn(Optional.of(userBook));
        when(userBookRepository.save(any(UserBook.class))).thenAnswer(i -> i.getArgument(0));

        UserBook result = userService.updateReadStatus(book.getIsbn(), "testUser", true);

        assertThat(result.isReadStatus()).isTrue();
        verify(userBookRepository).save(userBook);
    }

    @Test
    void updateReadStatus_notOwned_throws() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserIdAndBookIsbn(user.getId(), book.getIsbn()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateReadStatus(book.getIsbn(), "testUser", true))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("User does not own this book");
    }

    @Test
    void deleteBook_bookExists_deletesAll() {
        when(bookRepository.existsById(book.getIsbn())).thenReturn(true);

        userService.deleteBook(book.getIsbn());

        verify(userBookRepository).deleteByBookIsbn(book.getIsbn());
        verify(bookRepository).deleteById(book.getIsbn());
    }

    @Test
    void deleteBook_bookNotFound_throws() {
        when(bookRepository.existsById(book.getIsbn())).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteBook(book.getIsbn()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Book not found");
    }

    @Test
    void removeBookFromUser_ownedByUserAndNoOtherUsers_deletesBook() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserIdAndBookIsbn(user.getId(), book.getIsbn()))
                .thenReturn(Optional.of(userBook));
        when(userBookRepository.existsByBookIsbn(book.getIsbn())).thenReturn(false);

        userService.removeBookFromUser(book.getIsbn(), "testUser");

        verify(userBookRepository).delete(userBook);
        verify(bookRepository).deleteById(book.getIsbn());
    }

    @Test
    void removeBookFromUser_ownedByUserAndOtherUsers_doesNotDeleteBook() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserIdAndBookIsbn(user.getId(), book.getIsbn()))
                .thenReturn(Optional.of(userBook));
        when(userBookRepository.existsByBookIsbn(book.getIsbn())).thenReturn(true);

        userService.removeBookFromUser(book.getIsbn(), "testUser");

        verify(userBookRepository).delete(userBook);
        verify(bookRepository, never()).deleteById(any());
    }

    @Test
    void removeBookFromUser_notOwned_throws() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(userBookRepository.findByUserIdAndBookIsbn(user.getId(), book.getIsbn()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.removeBookFromUser(book.getIsbn(), "testUser"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("User does not own this book");
    }
}
