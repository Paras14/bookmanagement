package com.bookkeeping.bookmanagement.Bookpackage.dtos;

import lombok.Data;

@Data
public class ChatResponseDTO {
    private String answer;

    // Constructor
    public ChatResponseDTO(String answer) {
        this.answer = answer;
    }

    // Getters and setters
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}