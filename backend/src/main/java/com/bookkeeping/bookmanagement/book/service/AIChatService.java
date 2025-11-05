package com.bookkeeping.bookmanagement.book.service;

import com.bookkeeping.bookmanagement.book.dtos.UserBookDTO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIChatService {

    private final ChatClient chatClient;
    private final BookService bookService;

    public AIChatService(ChatClient.Builder builder, BookService bookService) {
        this.chatClient = builder
                .defaultSystem("You are a helpful assistant knowledgeable about books, but not all of them")
                .build();
        this.bookService = bookService;
    }

    public String getChatResponse(String isbn, String question, String username) {
        UserBookDTO book = bookService.getUserBooksByIsbn(isbn, username)
                .orElseThrow(() -> new RuntimeException("Book not found or user doesn't have access"));

        String message = String.format(
                "The current context is about the book: '%s' by author %s. Please answer the following question about this book: %s",
                book.getBookName(), book.getAuthorName(), question
        );

        return chatClient.prompt().user(message).call().content();
    }
}
