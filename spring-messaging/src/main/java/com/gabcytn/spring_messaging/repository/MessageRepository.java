package com.gabcytn.spring_messaging.repository;

import com.gabcytn.spring_messaging.model.PrivateMessage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public List<PrivateMessage> findAllByConversationId (int conversationId) {
        final String sqlQuery = """
                SELECT
                    conversation_id, users.username AS sender, message, sent_at
                FROM
                    messages
                JOIN
                    users
                ON
                    users.id = messages.sender_id
                WHERE
                    conversation_id = ?
                """;
        final RowMapper<PrivateMessage> privateMessageRowMapper = (rs, rowNum) -> new PrivateMessage(
            rs.getInt("conversation_id"),
            rs.getString("sender"),
            rs.getString("message"),
            false,
            rs.getTimestamp("sent_at")
        );

        return jdbcTemplate.query(sqlQuery, privateMessageRowMapper, conversationId);
    }
}
