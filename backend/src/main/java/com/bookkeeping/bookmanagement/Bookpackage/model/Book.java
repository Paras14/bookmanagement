package com.bookkeeping.bookmanagement.Bookpackage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(max = 14, min = 10, message = "Enter a Valid ISBN")
    private String isbn;

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
