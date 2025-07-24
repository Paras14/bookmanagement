package com.bookkeeping.bookmanagement.book.controller;

import com.bookkeeping.bookmanagement.book.configuration.SecurityConfig;
import com.bookkeeping.bookmanagement.book.configuration.jwtConfig.JwtAuthenticationFilter;
import com.bookkeeping.bookmanagement.book.dtos.BookDTO;
import com.bookkeeping.bookmanagement.book.dtos.UserBookDTO;
import com.bookkeeping.bookmanagement.book.model.Book.Genre;
import com.bookkeeping.bookmanagement.book.service.BookService;
import com.bookkeeping.bookmanagement.book.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void stubJwtFilter() throws Exception {
        doAnswer(inv -> {
            ServletRequest req = inv.getArgument(0);
            ServletResponse res = inv.getArgument(1);
            FilterChain chain = inv.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(jwtAuthenticationFilter)
                .doFilter(any(ServletRequest.class),
                        any(ServletResponse.class),
                        any(FilterChain.class));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "ADMIN")
    void getAllBooks_asAdmin_shouldReturnList() throws Exception {
        var dto = new BookDTO();
        dto.setIsbn("isbn-1");
        dto.setBookName("Title One");
        dto.setAuthorName("Author A");
        dto.setGenre(Genre.COMEDY);

        when(bookService.getAllBooks()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/books/admin/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].isbn").value("isbn-1"))
                .andExpect(jsonPath("$[0].bookName").value("Title One"))
                .andExpect(jsonPath("$[0].authorName").value("Author A"))
                .andExpect(jsonPath("$[0].genre").value("COMEDY"));

        verify(bookService).getAllBooks();
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void getUserBooks_asUser_shouldReturnList() throws Exception {
        var dto = new UserBookDTO();
        dto.setIsbn("isbn-2");
        dto.setBookName("Title Two");
        dto.setAuthorName("Author B");
        dto.setGenre(Genre.THRILLER);
        dto.setReadStatus(true);

        when(bookService.getUserBooks("testUser"))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/books")
                        .with(csrf()))  // GET doesn’t need CSRF, but harmless
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("isbn-2"))
                .andExpect(jsonPath("$[0].bookName").value("Title Two"))
                .andExpect(jsonPath("$[0].authorName").value("Author B"))
                .andExpect(jsonPath("$[0].genre").value("THRILLER"))
                .andExpect(jsonPath("$[0].readStatus").value(true));

        verify(bookService).getUserBooks("testUser");
    }

    @Test
    @WithMockUser(username = "testUser")
    void getUserBookByIsbn_found_shouldReturnOk() throws Exception {
        var dto = new UserBookDTO();
        dto.setIsbn("isbn-3");
        dto.setBookName("Title Three");
        dto.setAuthorName("Author C");
        dto.setGenre(Genre.MYSTERY);
        dto.setReadStatus(false);

        when(bookService.getUserBooksByIsbn("isbn-3", "testUser"))
                .thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/books/{isbn}", "isbn-3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("isbn-3"))
                .andExpect(jsonPath("$.bookName").value("Title Three"))
                .andExpect(jsonPath("$.authorName").value("Author C"))
                .andExpect(jsonPath("$.genre").value("MYSTERY"))
                .andExpect(jsonPath("$.readStatus").value(false));

        verify(bookService).getUserBooksByIsbn("isbn-3", "testUser");
    }

    @Test
    @WithMockUser(username = "testUser")
    void getUserBookByIsbn_notFound_shouldReturn404() throws Exception {
        when(bookService.getUserBooksByIsbn("unknown", "testUser"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/{isbn}", "unknown"))
                .andExpect(status().isNotFound());

        verify(bookService).getUserBooksByIsbn("unknown", "testUser");
    }

    @Test
    @WithMockUser(username = "testUser", roles = "ADMIN")
    void deleteBook_asAdmin_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/books/admin/{isbn}", "isbn-4")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(bookService).deleteBook("isbn-4");
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void removeBookFromUser_asUser_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/books/{isbn}", "isbn-5")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(bookService).removeBookFromUser("isbn-5", "testUser");
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void addBookToUser_asUser_shouldReturnOk() throws Exception {
        var request = new BookDTO();
        request.setIsbn("isbn-6");
        request.setBookName("Title Six");
        request.setAuthorName("Author F");
        request.setGenre(Genre.FANTASY);

        var response = new UserBookDTO();
        response.setIsbn("isbn-6");
        response.setBookName("Title Six");
        response.setAuthorName("Author F");
        response.setGenre(Genre.FANTASY);
        response.setReadStatus(false);

        when(bookService.addBookToUser(eq(request), eq("testUser")))
                .thenReturn(response);

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isbn").value("isbn-6"))
                .andExpect(jsonPath("$.bookName").value("Title Six"))
                .andExpect(jsonPath("$.authorName").value("Author F"))
                .andExpect(jsonPath("$.genre").value("FANTASY"))
                .andExpect(jsonPath("$.readStatus").value(false));

        verify(bookService).addBookToUser(eq(request), eq("testUser"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void editBookReadStatus_asUser_found_shouldReturnOk() throws Exception {
        var request = new UserBookDTO();
        request.setIsbn("isbn-7");
        request.setBookName("Title Seven");
        request.setAuthorName("Author G");
        request.setGenre(Genre.SCIENCE_FICTION);
        request.setReadStatus(true);

        when(bookService.updateReadStatus("isbn-7", "testUser", true))
                .thenReturn(Optional.of(request));

        mockMvc.perform(put("/api/books/{isbn}", "isbn-7")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("isbn-7"))
                .andExpect(jsonPath("$.readStatus").value(true));

        verify(bookService).updateReadStatus("isbn-7", "testUser", true);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void editBookReadStatus_asUser_notFound_shouldReturn404() throws Exception {
        var request = new UserBookDTO();
        request.setIsbn("isbn-8");
        request.setBookName("Title Eight");
        request.setAuthorName("Author H");
        request.setGenre(Genre.ROMANCE);
        request.setReadStatus(false);

        when(bookService.updateReadStatus("isbn-8", "testUser", false))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/books/{isbn}", "isbn-8")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(bookService).updateReadStatus("isbn-8", "testUser", false);
    }
}
