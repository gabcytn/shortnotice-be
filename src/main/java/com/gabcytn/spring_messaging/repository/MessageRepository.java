package com.gabcytn.spring_messaging.repository;

import com.gabcytn.spring_messaging.model.OutgoingMessage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MessageRepository {
    private final JdbcTemplate jdbcTemplate;

    public MessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer save(int conversationId, UUID sender, String message) {
        final String sqlQuery = """
                INSERT INTO messages (conversation_id, sender_id, message)
                VALUES (?, ?, ?)
                RETURNING id
                """;
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, conversationId, sender, message);
    }

    public List<OutgoingMessage> findAllByConversationId(int conversationId, int cursor) {
        final String sqlQuery = """
                SELECT
                    conversation_id, users.username AS sender, message, messages.id AS message_id, sent_at
                FROM
                    messages
                JOIN
                    users
                ON
                    users.id = messages.sender_id
                WHERE
                    conversation_id = ?
                AND
                    messages.id < ?
                ORDER BY
                    messages.sent_at DESC
                LIMIT 20
                """;
        final RowMapper<OutgoingMessage> privateMessageRowMapper = (rs, rowNum) -> new OutgoingMessage(
                rs.getInt("conversation_id"),
                rs.getString("sender"),
                rs.getString("message"),
                rs.getInt("message_id"),
                false,
                rs.getTimestamp("sent_at"));

        return jdbcTemplate.query(sqlQuery, privateMessageRowMapper, conversationId, cursor);
    }

    public Integer getLastMessageId () {
        final String sqlQuery = """
                SELECT
                    id
                FROM
                    messages
                ORDER BY
                    id DESC
                LIMIT 1
                """;

        ResultSetExtractor<Integer> extractor = (rs) -> rs.next() ? rs.getInt("id") : 0;
        return jdbcTemplate.query(sqlQuery, extractor);
    }
}
