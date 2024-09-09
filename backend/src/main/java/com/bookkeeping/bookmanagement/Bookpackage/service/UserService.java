package com.bookkeeping.bookmanagement.Bookpackage.service;

import com.bookkeeping.bookmanagement.Bookpackage.model.Book;
import com.bookkeeping.bookmanagement.Bookpackage.model.Users;
import com.bookkeeping.bookmanagement.Bookpackage.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);


    public Users register(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Set<Book> getUserBooks(String username){
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getBooks();
    }
}
