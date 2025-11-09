package com.bookkeeping.bookmanagement.book.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Book name can't be empty")
    private String bookName;

    @NotBlank(message = "Author name can't be empty")
    private String authorName;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    public enum Genre{
        COMEDY,
        THRILLER,
        SELF_HELP,
        SCIENCE_FICTION,
        MYSTERY,
        ROMANCE,
        FANTASY
    }


}
