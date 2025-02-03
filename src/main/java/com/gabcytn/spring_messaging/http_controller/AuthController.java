package com.gabcytn.spring_messaging.http_controller;

import com.gabcytn.spring_messaging.model.User;
import com.gabcytn.spring_messaging.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody User user,
            HttpServletRequest request,
            HttpServletResponse response) {
        return authService.authenticate(user, request, response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody User user) {
        return authService.register(user);
    }

    @GetMapping("/credentials")
    public Map<String, Object> getUsername(HttpServletRequest request) {
        final Map<String, Object> response = new HashMap<>();
        final UUID id = UUID.fromString((String) request.getSession().getAttribute("uuid"));
        final String username = (String) request.getSession().getAttribute("username");

        response.put("id", id);
        response.put("username", username);
        return response;
    }

    @GetMapping("/auth/status")
    public Boolean getAuthStatus(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session != null && session.getAttribute("uuid") != null;
    }
}
