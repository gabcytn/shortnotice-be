package com.gabcytn.spring_messaging.service;

import com.gabcytn.spring_messaging.model.UserPrincipal;
import com.gabcytn.spring_messaging.model.User;
import com.gabcytn.spring_messaging.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return new UserPrincipal(user.get());
        }

        throw new UsernameNotFoundException("User not found");
    }

    public ResponseEntity<List<User>> searchByUsername (HttpServletRequest request, String username) {
        try {
            final UUID requesterId = UUID.fromString((String) request.getSession().getAttribute("uuid"));
            final List<User> users = userRepository.findByUsernameContainingIgnoreCase(requesterId, username);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error searching for users");
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
