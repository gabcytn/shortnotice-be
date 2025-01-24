package com.gabcytn.spring_messaging.repository;

import com.gabcytn.spring_messaging.model.Conversation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public class ConversationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public ConversationsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer create() {
        final String sqlQuery = """
                INSERT INTO conversations (request)
                VALUES (true)
                RETURNING id
                """;
        final RowMapper<Integer> idRowMapper = (rs, rowNum) -> rs.getInt("id");

        return jdbcTemplate.queryForObject(sqlQuery, idRowMapper);
    }

    public void setRequestFalseById(int id) {
        final String sqlQuery = """
                UPDATE conversations
                SET request = false
                WHERE id = ?
                """;
        jdbcTemplate.update(sqlQuery, id);
    }

    public void saveMembers(int conversationId, UUID user1, UUID user2) {
        final String sqlQuery = """
                INSERT INTO conversation_members (conversation_id, user_id)
                VALUES (?, ?), (?, ?)
                """;
        jdbcTemplate.update(sqlQuery, conversationId, user1, conversationId, user2);
    }

    public Integer existsByMembers(UUID user1, UUID user2) {
        final String sqlQuery = """
                SELECT
                    cm1.conversation_id
                FROM
                    conversation_members AS cm1
                JOIN
                    conversation_members AS cm2
                ON
                    cm1.conversation_id = cm2.conversation_id
                WHERE
                    cm1.user_id = ?
                    AND
                    cm2.user_id = ?
                """;
        final ResultSetExtractor<Integer> extractor = (rs) -> rs.next() ? rs.getInt("conversation_id") : 0;
        final Object result = jdbcTemplate.query(sqlQuery, extractor, user1, user2);
        return result != null ? (int) result : 0;
    }

    public Boolean existsById(int id) {
        final String sqlQuery = """
                SELECT
                    COUNT(id) AS id
                FROM
                    conversations
                WHERE
                    id = ?
                """;
        final ResultSetExtractor<Integer> extractor = (rs) -> rs.next() ? rs.getInt("count") : 0;
        Object result = jdbcTemplate.query(sqlQuery, extractor, id);

        return result != null && (int) result > 0;
    }

    public List<Conversation> findByIdAndRequestTrueOrFalse(UUID requesterId, boolean isRequest) {
        final String sqlQuery = """
                WITH RecentMessages AS (
                    SELECT
                        messages.conversation_id,
                        messages.message,
                        messages.sent_at,
                        ROW_NUMBER() OVER (PARTITION BY messages.conversation_id ORDER BY messages.sent_at DESC) AS rn
                    FROM
                        messages
                )
                SELECT
                	DISTINCT(cm1.conversation_id),
                	users.username,
                        users.profile_pic,
                	rm.message,
                	rm.sent_at
                FROM
                	conversation_members AS cm1
                JOIN
                	conversation_members AS cm2
                ON
                	cm1.conversation_id = cm2.conversation_id
                JOIN
                	users
                ON
                	cm2.user_id = users.id
                JOIN
                	RecentMessages AS rm
                ON
                	cm1.conversation_id = rm.conversation_id
                JOIN
                	conversations
                ON
                	conversations.id = cm2.conversation_id
                WHERE
                	cm1.user_id = ?
                	AND
                	cm2.user_id <> ?
                	AND
                	conversations.request = ?
                    AND
                    rm.rn = 1
                ORDER BY
                	rm.sent_at DESC;
                """;
        final RowMapper<Conversation> conversationRowMapper = (rs, rowNum) -> {
            final int id = rs.getInt("conversation_id");
            final String username = rs.getString("username");
            final String message = rs.getString("message");
            final String avatar = rs.getString("profile_pic");
            final Timestamp timestamp = rs.getTimestamp("sent_at");

            return new Conversation(id, username, message, avatar, timestamp);
        };
        return jdbcTemplate.query(sqlQuery, conversationRowMapper, requesterId, requesterId, isRequest);
    }
}
