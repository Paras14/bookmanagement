package com.bookkeeping.bookmanagement.book.controller;

import com.bookkeeping.bookmanagement.book.service.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

//    @PostMapping(value = "/register")
//    public Users register(@RequestBody Users user){
//        return userService.register(user);
//    }
}
