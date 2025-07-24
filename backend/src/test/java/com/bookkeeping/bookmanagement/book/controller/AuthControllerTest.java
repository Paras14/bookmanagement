package com.bookkeeping.bookmanagement.book.controller;

import com.bookkeeping.bookmanagement.book.configuration.SecurityConfig;
import com.bookkeeping.bookmanagement.book.configuration.jwtConfig.JwtAuthenticationFilter;
import com.bookkeeping.bookmanagement.book.configuration.jwtConfig.JwtUtil;
import com.bookkeeping.bookmanagement.book.model.Users;
import com.bookkeeping.bookmanagement.book.service.UserDetailsServiceImpl;
import com.bookkeeping.bookmanagement.book.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void stubJwtFilter() throws Exception {
        doAnswer(invocation -> {
            ServletRequest req = invocation.getArgument(0);
            ServletResponse res = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(jwtAuthenticationFilter)
                .doFilter(any(), any(), any());
    }

    @Test
    @DisplayName("POST /api/auth/register → 201 CREATED + returned user")
    void registerUser() throws Exception {
        String payload = """
            {
              "username": "TestUser",
              "password": "Password1"
            }
            """;

        Users saved = new Users();
        saved.setId(1L);
        saved.setUsername("TestUser");
        saved.setPassword("$2a$...");

        when(userService.register(any(Users.class))).thenReturn(saved);

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("TestUser"));

        verify(userService).register(any(Users.class));
    }

    @Test
    @DisplayName("POST /api/auth/login → 200 OK + token on success")
    void loginUserSuccess() throws Exception {
        String payload = """
            {
              "username": "BobUser",
              "password": "Secure1"
            }
            """;

        var principal = User.withUsername("BobUser")
                .password("ignore")
                .authorities("ROLE_USER")
                .build();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities()
        );

        when(authenticationManager.authenticate(any()))
                .thenReturn(auth);
        when(jwtUtil.generateToken(principal))
                .thenReturn("jwt-token-123");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("jwt-token-123"));

        verify(authenticationManager).authenticate(any());
        verify(jwtUtil).generateToken(principal);
    }

    @Test
    @DisplayName("POST /api/auth/login → 401 UNAUTHORIZED on bad creds")
    void loginUserFailure() throws Exception {
        String payload = """
            {
              "username": "Invalid",
              "password": "Nope1"
            }
            """;

        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    @DisplayName("POST /api/auth/logout → 200 OK + message")
    void logout() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                        .with(csrf())
                        .header("Authorization", "Bearer some-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));
    }

    @Test
    @DisplayName("POST /api/auth/register → 400 Bad Request on invalid username")
    void registerUser_invalidUsername_tooShort() throws Exception {
        String payload = """
            {
              "username": "bob",
              "password": "Secret1"
            }
            """;

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/register → 400 Bad Request on invalid password")
    void registerUser_invalidPassword_missingNumber() throws Exception {
        String payload = """
            {
              "username": "validUser",
              "password": "NoNumberHere"
            }
            """;

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }
}
