package com.gabcytn.spring_messaging.repository;

import com.gabcytn.spring_messaging.model.PrivateMessageView;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public class MessageRepository {
    private final JdbcTemplate jdbcTemplate;

    public MessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void savePrivateMessage (UUID senderId, UUID receiverId, String message, Timestamp timestamp) {
        final String sqlQuery = """
                INSERT INTO messages (sender_id, receiver_id, message, sent_at)
                VALUES (?, ?, ?, ?)
                """;
        jdbcTemplate.update(sqlQuery, senderId, receiverId, message, timestamp);
    }

    public List<PrivateMessageView> findPrivateMessagesById (String sender, String receiver) {
        final String sqlQuery = """
                SELECT * FROM private_messages_view
                WHERE sender = ? AND receiver = ?
                ORDER BY sent_at DESC
                LIMIT 15
                """;
        return jdbcTemplate.query(sqlQuery, privateMessageViewRowMapper(), sender, receiver);
    }

    private RowMapper<PrivateMessageView> privateMessageViewRowMapper () {
        return (rs, rowNum) -> {
            final PrivateMessageView privateMessageView = new PrivateMessageView();
            privateMessageView.setSender(rs.getString("sender"));
            privateMessageView.setReceiver(rs.getString("receiver"));
            privateMessageView.setMessage(rs.getString("message"));
            privateMessageView.setSentAt(rs.getTimestamp("sent_at"));

            return privateMessageView;
        };
    }
}
