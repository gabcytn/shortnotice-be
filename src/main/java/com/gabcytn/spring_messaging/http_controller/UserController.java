package com.gabcytn.spring_messaging.http_controller;

import com.gabcytn.spring_messaging.model.User;
import com.gabcytn.spring_messaging.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchByUsername (@RequestParam(value = "username") String username, HttpServletRequest request) {
        return userService.searchByUsername(request, username);
    }
}
