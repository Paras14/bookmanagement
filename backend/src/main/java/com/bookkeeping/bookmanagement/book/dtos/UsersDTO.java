package com.bookkeeping.bookmanagement.book.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsersDTO {
    @NotBlank
    @Size(min = 4, max = 20, message = "Username name must be within 4 to 20 characters")
    private String username;

    @NotBlank
    @Size(min = 4, max = 20, message = "Password name must be within 4 to 20 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "Password must contain at least one uppercase letter, one lowercase letter, and one number")
    private String password;
}
