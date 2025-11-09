package com.bookkeeping.bookmanagement.book.service;

import com.bookkeeping.bookmanagement.book.dtos.UserBookDTO;
import com.bookkeeping.bookmanagement.book.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        Long bookId = 12345L;
        String username = "JohnDoe";
        String question = "What is the main theme?";

        UserBookDTO dto = new UserBookDTO();
        dto.setId(bookId);
        dto.setBookName("BookTitle");
        dto.setAuthorName("AuthorX");
        dto.setGenre(Book.Genre.MYSTERY);
        dto.setReadStatus(true);

        when(bookService.getUserBookById(bookId, username)).thenReturn(Optional.of(dto));
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn("This is an AI response");

        String result = aiChatService.getChatResponse(bookId, question, username);

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
        when(bookService.getUserBookById(999L, "testUser")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                aiChatService.getChatResponse(999L, "Who wrote this?", "testUser")
        ).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Book not found");
    }
}
