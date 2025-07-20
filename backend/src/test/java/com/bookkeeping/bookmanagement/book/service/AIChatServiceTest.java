package com.bookkeeping.bookmanagement.book.service;

import com.bookkeeping.bookmanagement.book.dtos.UserBookDTO;
import com.bookkeeping.bookmanagement.book.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AIChatServiceTest {

    @Mock
    private ChatClient.Builder chatClientBuilder;
    @Mock
    private ChatClient chatClient;
    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;
    @Mock
    private ChatClient.CallResponseSpec responseSpec;
    @Mock
    private BookService bookService;

    private AIChatService aiChatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(chatClientBuilder.defaultSystem(anyString())).thenReturn(chatClientBuilder);
        when(chatClientBuilder.build()).thenReturn(chatClient);

        aiChatService = new AIChatService(chatClientBuilder, bookService);
    }

    @Test
    void getChatResponse_whenBookFound_returnsAIResponse() {
        String isbn = "isbn-12345";
        String username = "JohnDoe";
        String question = "What is the main theme?";

        UserBookDTO dto = new UserBookDTO();
        dto.setIsbn(isbn);
        dto.setBookName("BookTitle");
        dto.setAuthorName("AuthorX");
        dto.setGenre(Book.Genre.valueOf("FANTASY"));
        dto.setReadStatus(true);

        when(bookService.getUserBooksByIsbn(isbn, username)).thenReturn(Optional.of(dto));
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn("This is an AI response");

        String result = aiChatService.getChatResponse(isbn, question, username);

        assertThat(result).isEqualTo("This is an AI response");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(requestSpec).user(captor.capture());
        String usedMessage = captor.getValue();
        assertThat(usedMessage)
                .contains("BookTitle")
                .contains("AuthorX")
                .contains(question);
    }

    @Test
    void getChatResponse_whenBookNotFound_throws() {
        when(bookService.getUserBooksByIsbn("isbn123456", "testUser")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                aiChatService.getChatResponse("isbn123456", "Who wrote this?", "testUser")
        ).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Book not found");
    }
}
