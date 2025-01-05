package com.gabcytn.spring_messaging.repository;

import com.gabcytn.spring_messaging.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findById(UUID uuid) {
        final String sqlQuery = """
                SELECT *
                FROM users
                WHERE id = ?
                """;
        return Optional.ofNullable(jdbcTemplate.query(sqlQuery, userResultSetExtractor(), uuid.toString()));
    }

    public Optional<User> findByUsername(String username) {
        String sqlQuery = """
                SELECT *
                FROM users
                WHERE username = ?
                """;
        return Optional.ofNullable(jdbcTemplate.query(sqlQuery, userResultSetExtractor(), username));
    }

    public void save(User user) {
        String sqlQuery = """
                INSERT INTO users (id, username, password)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(sqlQuery, user.getId(), user.getUsername(), user.getPassword());
    }

    private ResultSetExtractor<User> userResultSetExtractor() {
        return rs -> {
            if (rs.next()) {
                User user = new User();
                user.setId(UUID.fromString(rs.getString("id")));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setProfilePic(rs.getString("profile_pic"));
                user.setRole(rs.getString("role"));
                return user;
            }
            return null;
        };
    }
}
