package com.gabcytn.spring_messaging.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class MessageRequestRepository {
    private final JdbcTemplate jdbcTemplate;

    public MessageRequestRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save (UUID senderId, UUID recipientId, String message) {
            final String sqlQuery = """
                INSERT INTO message_requests
                (sender, recipient, message)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(sqlQuery, senderId, recipientId, message);
    }
}
