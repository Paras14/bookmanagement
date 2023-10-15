package com.bookkeeping.bookmanagement.Bookpackage;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.bookkeeping.bookmanagement.Bookpackage.Book.Genre.*;

@Service
public class BookService {

    private static List<Book> books = new ArrayList<>();

    static {
        books.add(new Book("978-1840227802", "Alice in a Wonderland", "Lewis Carroll", FANTASY, false));
        books.add(new Book("978-0735211292", "Atomic Habits", "James Clear", SELF_HELP, true));
        books.add(new Book("978-1544507873", "Can't Hurt Me", "David Goggins", SELF_HELP, true));
        books.add(new Book("978-0142424179", "The Fault in Our Stars", "John Green", ROMANCE, false));
        books.add(new Book("978-0307887436","Ready Player One", "Ernest Cline", ROMANCE, false));

    }

    public Book findByIsbn(String isbn){
        Predicate<?super Book> predicate =
                book -> book.getIsbn().equals(isbn);
        return books.stream().filter(predicate).findFirst().get();
    }

    public List<Book> retrieveAllBooks(){
        return books;
    }

    public boolean deleteByIsbn(String isbn){
        Predicate<? super Book> predicate = book -> book.getIsbn().equals(isbn);
        boolean isBookPresent = books.stream().anyMatch(predicate);
        books.removeIf(predicate);
        return isBookPresent;  // Return whether a book was deleted
    }

    public Book addBook(String isbn, String bookName, String authorName, Book.Genre genre, boolean readStatus){
        Book book = new Book(isbn, bookName, authorName, genre, readStatus);
        books.add(book);
        return book;
    }

}
