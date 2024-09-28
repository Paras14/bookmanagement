package com.bookkeeping.bookmanagement.Bookpackage.dtos;

import lombok.Data;

@Data
public class ChatRequestDTO {
    private String question;

    // Getters and setters
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
