package com.gabcytn.spring_messaging.http_controller;

import com.gabcytn.spring_messaging.model.User;
import com.gabcytn.spring_messaging.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController (AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login (
            @RequestBody User user,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authService.authenticate(user, request, response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register (@RequestBody User user) {
        return authService.register(user);
    }

    @GetMapping("/session")
    public String sessionId (HttpServletRequest request) {
        return request.getSession().getId();
    }

    @GetMapping("/uuid")
    public UUID getUsername(HttpServletRequest request) {
        return UUID.fromString((String) request.getSession().getAttribute("username"));
    }
}
