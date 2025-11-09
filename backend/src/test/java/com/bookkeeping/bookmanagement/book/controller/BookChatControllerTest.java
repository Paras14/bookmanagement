package com.bookkeeping.bookmanagement.book.controller;

import com.bookkeeping.bookmanagement.book.configuration.jwtConfig.JwtAuthenticationFilter;
import com.bookkeeping.bookmanagement.book.dtos.ChatRequestDTO;
import com.bookkeeping.bookmanagement.book.service.AIChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookChatController.class)
class BookChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AIChatService aiChatService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUpJwtFilter() throws Exception {
        doAnswer(invocation -> {
            ServletRequest req = invocation.getArgument(0);
            ServletResponse res = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(jwtAuthenticationFilter)
                .doFilter(any(ServletRequest.class), any(ServletResponse.class), any(FilterChain.class));
    }

    @Test
    @WithMockUser(username = "testUser")
    void chatAboutBook_shouldReturnChatResponse() throws Exception {
        Long bookId = 12345L;
        String question = "What is it about?";
        String expectedResponse = "It is about testing.";

        when(aiChatService.getChatResponse(bookId, question, "testUser"))
                .thenReturn(expectedResponse);

        ChatRequestDTO requestDto = new ChatRequestDTO();
        requestDto.setQuestion(question);

        mockMvc.perform(post("/api/books/chat/{bookId}", bookId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value(expectedResponse));

        verify(aiChatService, times(1)).getChatResponse(bookId, question, "testUser");
    }

    @Test
    void chatAboutBook_whenUnauthenticated_shouldReturnUnauthorized() throws Exception {
        Long bookId = 12345L;
        ChatRequestDTO requestDto = new ChatRequestDTO();
        requestDto.setQuestion("Hello");

        mockMvc.perform(post("/api/books/chat/{bookId}", bookId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(aiChatService);
    }
}
