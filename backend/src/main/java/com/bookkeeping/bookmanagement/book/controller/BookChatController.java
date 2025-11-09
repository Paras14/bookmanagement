package com.bookkeeping.bookmanagement.book.controller;

import com.bookkeeping.bookmanagement.book.dtos.ChatRequestDTO;
import com.bookkeeping.bookmanagement.book.dtos.ChatResponseDTO;
import com.bookkeeping.bookmanagement.book.service.AIChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books/chat")
public class BookChatController {

    private final AIChatService aiChatService;

    public BookChatController(AIChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<ChatResponseDTO> chatAboutBook(
            @PathVariable Long bookId,
            @RequestBody ChatRequestDTO chatRequest,
            Authentication authentication) {
        String username = authentication.getName();
        String response = aiChatService.getChatResponse(bookId, chatRequest.getQuestion(), username);
        return ResponseEntity.ok(new ChatResponseDTO(response));
    }
}
