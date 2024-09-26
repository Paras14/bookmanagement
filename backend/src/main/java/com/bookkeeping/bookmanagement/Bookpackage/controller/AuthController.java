package com.bookkeeping.bookmanagement.Bookpackage.controller;

import com.bookkeeping.bookmanagement.Bookpackage.configuration.jwtConfig.JwtUtil;
import com.bookkeeping.bookmanagement.Bookpackage.model.Users;
import com.bookkeeping.bookmanagement.Bookpackage.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@RequestBody Users user) {
        Users registeredUser = userService.register(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Users user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            Map<String, String> response = new HashMap<>();
            response.put("token", jwt);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
