package com.gabcytn.spring_messaging.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class MessageRepository {
    private final JdbcTemplate jdbcTemplate;

    public MessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save (int conversationId, UUID sender, String message) {
        final String sqlQuery = """
                INSERT INTO messages (conversation_id, sender_id, message)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(sqlQuery, conversationId, sender, message);
    }
}
